package kr.blogspot.crowjdh.inspirationgen.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import kr.blogspot.crowjdh.inspirationgen.extensions.pauseIfPlaying
import kr.blogspot.crowjdh.inspirationgen.extensions.stopIfPlaying
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import kr.blogspot.crowjdh.inspirationgen.music.models.toMidiFile
import java.io.File
import kotlin.properties.Delegates

class MidiPlayFragment: Fragment(), MediaPlayer.OnPreparedListener {

    private var mPlayer = MediaPlayer()

    private var mCacheFile: File by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMediaPlayer()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        initCacheFile(activity)
    }

    fun playSheet(sheet: Sheet) {
        mPlayer.stopIfPlaying()

        sheet.toMidiFile().writeToFile(mCacheFile)

        playMediaPlayerWithFile(mCacheFile)
    }

    private fun initMediaPlayer() {
        mPlayer.setOnPreparedListener(this)
    }

    private fun initCacheFile(activity: FragmentActivity) {
        mCacheFile = File(activity.cacheDir, "temp.mid")
    }

    private fun playMediaPlayerWithFile(file: File) {
        mPlayer.reset()
        mPlayer.setDataSource(file.absolutePath)
        mPlayer.prepareAsync()
    }

    override fun onPause() {
        super.onPause()

        mPlayer.pauseIfPlaying()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
    }

    companion object Factory {
        fun create() = MidiPlayFragment()
    }
}
