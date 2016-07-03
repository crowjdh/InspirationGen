package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.midisupport.MidiFile
import kr.blogspot.crowjdh.midisupport.MidiTrack
import kr.blogspot.crowjdh.midisupport.event.MidiEvent
import kr.blogspot.crowjdh.midisupport.event.NoteOff
import kr.blogspot.crowjdh.midisupport.event.NoteOn
import kr.blogspot.crowjdh.midisupport.event.ProgramChange
import kr.blogspot.crowjdh.midisupport.event.meta.Tempo

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 8.
 *
 * MidiSupportAdaptiveExtension
 */

private val MIDI_DEFAULT_METER
        = kr.blogspot.crowjdh.midisupport.event.meta.TimeSignature.DEFAULT_METER
private val MIDI_DEFAULT_DIVISION
        = kr.blogspot.crowjdh.midisupport.event.meta.TimeSignature.DEFAULT_DIVISION

fun TimeSignature.toMidiTimeSignature(): kr.blogspot.crowjdh.midisupport.event.meta.TimeSignature {
    val midiTimeSignature = kr.blogspot.crowjdh.midisupport.event.meta.TimeSignature()
    midiTimeSignature.setTimeSignature(count, noteLength.length,
            MIDI_DEFAULT_METER, MIDI_DEFAULT_DIVISION)

    return midiTimeSignature
}

fun Program.toMidiEvent(channel: Int = 0) = ProgramChange(0L, channel, number)

fun Sheet.toMidiFile(): MidiFile = MidiFile(MidiFile.DEFAULT_RESOLUTION, arrayListOf(
        createSoundMidiTrack(), createClickMidiTrack()))

private fun Sheet.createSoundMidiTrack(): MidiTrack {
    val soundTrack = createBaseMidiTrack()

    var accumulatedTicks = 0L
    bars.forEach { bar ->
        bar.toEachMidiEvents(accumulatedTicks) { event, ticks ->
            soundTrack.insertEvent(event)
            accumulatedTicks += ticks
        }
    }
    return soundTrack
}

private fun Sheet.createClickMidiTrack(): MidiTrack {
    val clickChannel = 9

    val track = createBaseMidiTrack()
    track.insertEvent(Program.STEEL_DRUMS.toMidiEvent(clickChannel))

    var accumulatedTicks = 0L
    bars.map { it.timeSignature }.forEach {
        track.insertEvent(it.toMidiTimeSignature())
        for (idx in 1..it.count) {
            Note(it.noteLength, 37).toMidiOnOffNotes(accumulatedTicks,
                    channel = clickChannel) { on, off ->
                on.velocity = if (idx == 1) 127 else 90
                track.insertEvent(on)
                track.insertEvent(off)
            }
            accumulatedTicks += it.noteLength.ticks()
        }
    }

    return track
}

private fun Sheet.createBaseMidiTrack(): MidiTrack {
    val track = MidiTrack()
    val tempo = Tempo()
    tempo.bpm = bpm.toFloat()
    track.insertEvent(tempo)

    return track
}

fun Bar.toEachMidiEvents(startTicks: Long = 0L, block: (event: MidiEvent, ticks: Long) -> Unit) {
    // TODO: Consider reducing redundant time signatures
    block(timeSignature.toMidiTimeSignature(), 0L)
    block(program.toMidiEvent(), 0L)
    var accumulatedTicks = 0L
    notables.forEach {
        it.toMidiOnOffNotes(startTicks + accumulatedTicks) { on, off ->
            val ticks = off.tick - on.tick
            block(on, ticks)
            block(off, 0L)
            accumulatedTicks += ticks
        }
    }
}

fun Notable.toMidiOnOffNotes(startTicks: Long, channel: Int = 0,
                             block: (event: NoteOn, event: NoteOff) -> Unit) {
    val endTicks = startTicks + ticks
    val pitch = if (this is Note) pitch else 0
    block(NoteOn(startTicks, channel, pitch, 100), NoteOff(endTicks, channel, pitch, 0))
}
