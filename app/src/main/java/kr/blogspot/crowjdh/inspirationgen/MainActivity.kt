package kr.blogspot.crowjdh.inspirationgen

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kr.blogspot.crowjdh.inspirationgen.extensions.MidiFile
import kr.blogspot.crowjdh.inspirationgen.extensions.removeTracks
import java.io.File
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    val cacheFile = File(cacheDir, "temp.mid")

    var mPlayer: MediaPlayer by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMidiFile()
        initMediaPlayer()

        playMediaPalyer()
    }

    private fun initMidiFile() {
        val midiFile = MidiFile(resources, R.raw.midi_sound)
        midiFile.removeTracks(1, 4)
        midiFile.writeToFile(cacheFile)
    }

    private fun initMediaPlayer() {
        mPlayer = MediaPlayer()
        mPlayer.setDataSource(cacheFile.absolutePath)
        mPlayer.prepare()
    }

    private fun playMediaPalyer() {
        mPlayer.start()
    }
}
