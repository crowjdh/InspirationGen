package kr.blogspot.crowjdh.inspirationgen.ui


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SheetHistoryAdapter
import kotlin.properties.Delegates

class MainFragment: Fragment() {

    private val mSheetHistoryRecyclerView: RecyclerView by bindView(R.id.sheet_history)
    private var mLayoutManager: LinearLayoutManager by Delegates.notNull()
    private var mAdapter = SheetHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mAdapter.setOnItemClickListener(activity as? SheetHistoryAdapter.OnItemClickListener)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLayoutManager = LinearLayoutManager(context)
        mSheetHistoryRecyclerView.layoutManager = mLayoutManager
        mSheetHistoryRecyclerView.adapter = mAdapter
    }
}
