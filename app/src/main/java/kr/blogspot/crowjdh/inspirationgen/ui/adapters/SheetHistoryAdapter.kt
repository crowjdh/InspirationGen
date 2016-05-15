package kr.blogspot.crowjdh.inspirationgen.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 15.
 *
 * SheetHistoryAdapter
 */

class SheetHistoryAdapter(sheets: MutableList<Sheet> = mutableListOf()):
        RecyclerView.Adapter<SheetHistoryAdapter.SheetHistoryViewHolder>() {

    private val mSheets = sheets

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SheetHistoryViewHolder? {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.vh_sheet_history, parent, false)
        return SheetHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SheetHistoryViewHolder?, position: Int) {
        holder!!.numberView.text = position.toString()
        holder.contentView.text = mSheets[position].name
    }

    override fun getItemCount() = mSheets.count()

    fun prependSheet(sheet: Sheet) {
        mSheets.add(0, sheet)
    }

    class SheetHistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val numberView: TextView by bindView(R.id.number)
        val contentView: TextView by bindView(R.id.content)
    }
}
