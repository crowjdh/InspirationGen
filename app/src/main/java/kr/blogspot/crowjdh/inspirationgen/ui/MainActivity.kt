package kr.blogspot.crowjdh.inspirationgen.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.bindView
import com.jakewharton.rxbinding.view.clicks
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.extensions.insert
import kr.blogspot.crowjdh.inspirationgen.extensions.startActivity
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength
import kr.blogspot.crowjdh.inspirationgen.music.models.Scale
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SheetHistoryAdapter

class MainActivity: AppCompatActivity(), SheetHistoryAdapter.OnItemClickListener {

    private val midiPlayerFragmentTag = "midiPlayerFragmentTag"

    private val midiPlayFragment = MidiPlayFragment.create()

    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mGenerateFab: FloatingActionButton by bindView(R.id.generate);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mToolbar)

        initMidiPlayFragment()
        initActions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> startActivity(SettingsActivity::class)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initMidiPlayFragment() {
        supportFragmentManager.beginTransaction()
                .add(midiPlayFragment, midiPlayerFragmentTag).commit()
    }

    private fun initActions() {
        mGenerateFab.clicks().subscribe {
            val sheet = generateRandomSheet()
            sheet.insert()
            midiPlayFragment.playSheet(sheet)
        }
    }

    private fun generateRandomSheet(): Sheet {
        val sheet = Sheet()
        sheet.bars.addAll(Bar.generate {
            barCount = 2
            noteOverRestBias = .8f
            scale = Scale.major(Scale.C4)
            noteLengthRange = Bar.Generator.NoteLengthRange.create(
                    Pair(NoteLength.QUARTER, 20), Pair(NoteLength.EIGHTH, 80))
        })

        return sheet
    }

    override fun onItemClick(index: Int, item: Sheet) {
        midiPlayFragment.playSheet(item)
    }
}
