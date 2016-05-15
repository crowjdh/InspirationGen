package kr.blogspot.crowjdh.inspirationgen.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.bindView
import com.jakewharton.rxbinding.view.clicks
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.extensions.pauseIfPlaying
import kr.blogspot.crowjdh.inspirationgen.extensions.stopIfPlaying
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import kr.blogspot.crowjdh.inspirationgen.music.models.toMidiFile
import kr.blogspot.crowjdh.inspirationgen.ui.MainFragment
import java.io.File
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener {

    private var mCacheFile: File by Delegates.notNull()

    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mGenerateFab: FloatingActionButton by bindView(R.id.generate);

    private var mPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mToolbar)

        initMediaPlayer()
        initCacheFile()
        initActions()
    }

    private fun initMediaPlayer() {
        mPlayer.setOnPreparedListener(this)
    }

    private fun initCacheFile() {
        mCacheFile = File(cacheDir, "temp.mid")
    }

    private fun initActions() {
        mGenerateFab.clicks().subscribe {
            mPlayer.stopIfPlaying()

            val sheet = generateRandomSheet()
            sheet.toMidiFile(120f).writeToFile(mCacheFile)
            mainFragment.addSheet(sheet)

            playMediaPlayerWithFile(mCacheFile)
        }
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

    private fun generateRandomSheet(): Sheet {
        val sheet = Sheet()
        sheet.addBars(Bar.generate {
            barCount = 2
            noteOverRestBias = .8f
            noteLengthRange = Bar.Generator.NoteLengthRange.create(
                    Pair(NoteLength.QUARTER, 20), Pair(NoteLength.EIGHTH, 80))
        })

        return sheet
    }

    private val mainFragment: MainFragment
        get() = supportFragmentManager.findFragmentById(R.id.mainFragment) as MainFragment

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
    }
}
