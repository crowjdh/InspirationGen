package kr.blogspot.crowjdh.inspirationgen.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import kr.blogspot.crowjdh.inspirationgen.extensions.*
import kr.blogspot.crowjdh.inspirationgen.music.models.MasterSettings
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import kr.blogspot.crowjdh.inspirationgen.music.models.toMidiFile
import rx.Subscription
import java.io.File
import kotlin.properties.Delegates

class MidiPlayFragment: Fragment(), MediaPlayer.OnPreparedListener {

    private var mPlayer = MediaPlayer()

    private var mCacheFile: File by Delegates.notNull()
    private var mMasterSettings: MasterSettings by Delegates.notNull()
    private val mSubscriptions: MutableList<Subscription> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMediaPlayer()
        observeMasterOptions()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        initCacheFile(activity)
    }

    fun playSheet(sheet: Sheet) {
        mPlayer.stopIfPlaying()

        sheet.toMidiFile(mMasterSettings.enableClickTrack).writeToFile(mCacheFile)

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

    private fun observeMasterOptions() {
        mSubscriptions.add(database.observeTable<MasterSettings>(MasterSettings::class) {
            mMasterSettings = it.firstOrDefault(MasterSettings.default)
        })
    }

    private fun unObserveAllSubscriptions() {
        mSubscriptions.forEach { it.unsubscribe() }
    }

    override fun onPause() {
        super.onPause()

        mPlayer.pauseIfPlaying()
    }

    override fun onDestroy() {
        super.onDestroy()

        unObserveAllSubscriptions()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
    }

    companion object Factory {
        fun create() = MidiPlayFragment()
    }
}
