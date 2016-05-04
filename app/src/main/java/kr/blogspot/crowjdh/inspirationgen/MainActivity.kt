package kr.blogspot.crowjdh.inspirationgen

import android.media.MediaPlayer
import android.os.Bundle
import android.support.annotation.RawRes
import android.support.v7.app.AppCompatActivity
import kr.blogspot.crowjdh.inspirationgen.extensions.MidiFile
import kr.blogspot.crowjdh.inspirationgen.extensions.pauseIfPlaying
import kr.blogspot.crowjdh.inspirationgen.extensions.startIfNotPlaying
import kr.blogspot.crowjdh.inspirationgen.models.*
import kr.blogspot.crowjdh.midisupport.MidiFile
import kr.blogspot.crowjdh.midisupport.MidiTrack
import kr.blogspot.crowjdh.midisupport.event.meta.Tempo
import kr.blogspot.crowjdh.midisupport.event.meta.TimeSignature
import java.io.File
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    var mPlayer: MediaPlayer by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cacheFile = createCacheFile()
//        copyRawResourceIntoFile(R.raw.whistle_concert, cacheFile)
        createTempMidiFile().writeToFile(cacheFile)
        prepareMediaPlayer(cacheFile)

        testBar()
    }

    private fun testBar() {
        val bar = Bar()
        val barTwo = Bar(DEFAULT_TIME_SIGNATURE)
        //        val sheet = Sheet()
        //        sheet.addBar(bar)
        //
        for (i: Int in 40..50) {
            val noteLength = when(i % 3) {
                0 -> NoteLength.QUARTER
                else -> NoteLength.EIGHTH
            }
            val notable = when(i % 2) {
                0 -> Note(noteLength, i)
                else -> Rest(noteLength)
            }
            bar.addNotable(notable)
        }

        bar.timeSignature = kr.blogspot.crowjdh.inspirationgen.models.TimeSignature(5, NoteLength.QUARTER, 480)
        bar.timeSignature = kr.blogspot.crowjdh.inspirationgen.models.TimeSignature(3, NoteLength.QUARTER, 480)
        bar.timeSignature = kr.blogspot.crowjdh.inspirationgen.models.TimeSignature(4, NoteLength.QUARTER, 480)
    }

    override fun onResume() {
        super.onResume()

        playMediaPlayer()
    }

    override fun onPause() {
        super.onPause()

        pauseMediaPlayer()
    }

    private fun createCacheFile(): File = File(cacheDir, "temp.mid")

    private fun copyRawResourceIntoFile(@RawRes id: Int, file: File) {
        val midiFile = MidiFile(resources, id)
//        midiFile.resolution = 90
//        midiFile.removeTracks(1..3)
        midiFile.writeToFile(file)
    }

    private fun prepareMediaPlayer(file: File) {
        mPlayer = MediaPlayer()
        mPlayer.setDataSource(file.absolutePath)
        mPlayer.prepare()
    }

    private fun playMediaPlayer() {
        mPlayer.startIfNotPlaying()
    }

    private fun pauseMediaPlayer() {
        mPlayer.pauseIfPlaying()
    }

    private fun createTempMidiFile(): MidiFile {
        val tempoTrack = MidiTrack()
        val noteTrack = MidiTrack()

        val timeSignature = TimeSignature()
        timeSignature.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION)

        val tempo = Tempo()
        tempo.bpm = 120f

        tempoTrack.insertEvent(timeSignature)
        tempoTrack.insertEvent(tempo)

        for (idx in 0..24) {
            noteTrack.insertNote(0, idx + 44, 100, idx * 480L, 480L)
        }

        return MidiFile(MidiFile.DEFAULT_RESOLUTION, arrayListOf(tempoTrack, noteTrack))
    }
}
