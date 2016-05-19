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
import kr.blogspot.crowjdh.inspirationgen.extensions.*
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SheetHistoryAdapter
import rx.Subscription
import kotlin.properties.Delegates

class MainActivity: AppCompatActivity(), SheetHistoryAdapter.OnItemClickListener {

    private val midiPlayerFragmentTag = "midiPlayerFragmentTag"

    private val midiPlayFragment = MidiPlayFragment.create()

    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mGenerateFab: FloatingActionButton by bindView(R.id.generate);
    private var mSheetOptions: Sheet.Options by Delegates.notNull()
    private var mBarOptions: Bar.Generator.Options by Delegates.notNull()
    private val mSubscriptions: MutableList<Subscription> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mToolbar)

        initMidiPlayFragment()
        initActions()
        observeGenerationOptions()
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

    override fun onDestroy() {
        super.onDestroy()
        unObserveAllSubscriptions()
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

    private fun observeGenerationOptions() {
        mSubscriptions.add(database.observeTable<Sheet.Options>(Sheet.Options::class) {
            mSheetOptions = it.firstOrDefault(Sheet.Options.default)
        })
        mSubscriptions.add(database.observeTable<Bar.Generator.Options>(Bar.Generator.Options::class) {
            mBarOptions = it.firstOrDefault(Bar.Generator.Options.default)
        })
    }

    private fun unObserveAllSubscriptions() {
        mSubscriptions.forEach { it.unsubscribe() }
    }

    private fun generateRandomSheet(): Sheet {
        val sheet = Sheet(options = mSheetOptions)
        sheet.bars.addAll(Bar.generate(mBarOptions))

        return sheet
    }

    override fun onItemClick(index: Int, item: Sheet) {
        midiPlayFragment.playSheet(item)
    }
}
