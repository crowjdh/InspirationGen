package kr.blogspot.crowjdh.inspirationgen.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.bindView
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.ScaleSettingsAdapter
import kotlin.properties.Delegates

class ScaleSettingsActivity : AppCompatActivity() {

    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mScaleSettingsRecyclerView: RecyclerView by bindView(R.id.scale_settings)
    private var mLayoutManager: LinearLayoutManager by Delegates.notNull()
    private var mAdapter = ScaleSettingsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_scale)
        setSupportActionBar(mToolbar)

        mLayoutManager = LinearLayoutManager(applicationContext)
        mScaleSettingsRecyclerView.layoutManager = mLayoutManager
        mScaleSettingsRecyclerView.adapter = mAdapter
    }
}
