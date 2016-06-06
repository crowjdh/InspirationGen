package kr.blogspot.crowjdh.inspirationgen.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.bindView
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.GeneralSettingsAdapter
import kotlin.properties.Delegates

class SettingsActivity : AppCompatActivity() {

    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mSettingsRecyclerView: RecyclerView by bindView(R.id.settings)
    private var mLayoutManager: LinearLayoutManager by Delegates.notNull()
    private var mAdapter = GeneralSettingsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(mToolbar)

        mLayoutManager = LinearLayoutManager(applicationContext)
        mSettingsRecyclerView.layoutManager = mLayoutManager
        mSettingsRecyclerView.adapter = mAdapter
    }
}
