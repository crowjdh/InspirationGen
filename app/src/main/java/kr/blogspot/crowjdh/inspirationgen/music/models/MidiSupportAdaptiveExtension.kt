package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.midisupport.MidiFile
import kr.blogspot.crowjdh.midisupport.MidiTrack
import kr.blogspot.crowjdh.midisupport.event.MidiEvent
import kr.blogspot.crowjdh.midisupport.event.NoteOff
import kr.blogspot.crowjdh.midisupport.event.NoteOn
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

fun Sheet.toMidiFile(): MidiFile {
    val track = MidiTrack()
    val tempo = Tempo()
    tempo.bpm = bpm.toFloat()
    track.insertEvent(tempo)

    var accumulatedTicks = 0L
    bars.forEach { bar ->
        bar.toEachMidiEvents(accumulatedTicks) { event, ticks ->
            track.insertEvent(event)
            accumulatedTicks += ticks
        }
    }
    return MidiFile(MidiFile.DEFAULT_RESOLUTION, arrayListOf(track))
}

fun Bar.toEachMidiEvents(startTicks: Long = 0L, block: (event: MidiEvent, ticks: Long) -> Unit) {
    // TODO: Consider reducing redundant time signatures
    block(timeSignature.toMidiTimeSignature(), 0L)
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

fun Notable.toMidiOnOffNotes(startTicks: Long,
                             block: (event: NoteOn, event: NoteOff) -> Unit) {
    val endTicks = startTicks + ticks
    val pitch = if (this is Note) pitch else 0
    block(NoteOn(startTicks, 0, pitch, 100), NoteOff(endTicks, 0, pitch, 0))
}
