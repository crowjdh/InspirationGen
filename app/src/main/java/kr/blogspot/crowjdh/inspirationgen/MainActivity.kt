package kr.blogspot.crowjdh.inspirationgen

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kr.blogspot.crowjdh.inspirationgen.extensions.MidiFile
import kr.blogspot.crowjdh.inspirationgen.extensions.pauseIfPlaying
import kr.blogspot.crowjdh.inspirationgen.extensions.removeTracks
import kr.blogspot.crowjdh.inspirationgen.extensions.startIfNotPlaying
import java.io.File
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    var cacheFile: File by Delegates.notNull()

    var mPlayer: MediaPlayer by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCacheFile()
        initMidiFile()
        initMediaPlayer()
    }

    override fun onResume() {
        super.onResume()

        playMediaPlayer()
    }

    override fun onPause() {
        super.onPause()

        pauseMediaPlayer()
    }

    private fun initCacheFile() {
        cacheFile = File(cacheDir, "temp.mid")
    }

    private fun initMidiFile() {
        val midiFile = MidiFile(resources, R.raw.midi_sound)
        midiFile.removeTracks(1..3)
        midiFile.writeToFile(cacheFile)
    }

    private fun initMediaPlayer() {
        mPlayer = MediaPlayer()
        mPlayer.setDataSource(cacheFile.absolutePath)
        mPlayer.prepare()
    }

    private fun playMediaPlayer() {
        mPlayer.startIfNotPlaying()
    }

    private fun pauseMediaPlayer() {
        mPlayer.pauseIfPlaying()
    }
}
