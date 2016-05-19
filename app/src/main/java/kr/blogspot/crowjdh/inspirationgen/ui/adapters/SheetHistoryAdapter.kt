package kr.blogspot.crowjdh.inspirationgen.ui.adapters

import android.app.AlertDialog
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.longClicks
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.extensions.database
import kr.blogspot.crowjdh.inspirationgen.extensions.observeTable
import kr.blogspot.crowjdh.inspirationgen.extensions.update
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 15.
 *
 * SheetHistoryAdapter
 */

class SheetHistoryAdapter(): RecyclerView.Adapter<SheetHistoryAdapter.SheetHistoryViewHolder>() {

    init {
        database.observeTable<Sheet>(Sheet::class) {
            mSheets = it.reversed().sortedBy { !it.pinned }
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(index: Int, item: Sheet)
    }

    private var mSheets = emptyList<Sheet>()
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SheetHistoryViewHolder? {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.vh_sheet_history, parent, false)
        return SheetHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SheetHistoryViewHolder?, position: Int) {
        val item = mSheets[position]

        fillContents(holder!!, item, position)
        setActions(holder, item, position)
    }

    private fun fillContents(holder: SheetHistoryViewHolder, item: Sheet, position: Int) {
        holder.numberView.text = position.toString()
        holder.contentView.text = item.name
        holder.contentView.setTextColor(
                if (item.pinned) {
                    Color.parseColor("#aa4444")
                } else {
                    holder.numberView.textColors.defaultColor
                })
    }

    private fun setActions(holder: SheetHistoryViewHolder, item: Sheet, position: Int) {
        val itemName = holder.rootView.context.resources.getString(
                if (item.pinned) R.string.unpin else R.string.pin
        )
        holder.rootView.longClicks().subscribe {
            AlertDialog.Builder(holder.rootView.context)
                    .setTitle(R.string.actions)
                    .setItems(arrayOf(itemName)) { dialog, index ->
                        item.update {
                            pinned = !pinned
                        }
                    }
                    .show()
        }
        holder.playButton.clicks().subscribe { mOnItemClickListener?.onItemClick(position, item) }
    }

    override fun getItemCount() = mSheets.count()

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    class SheetHistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val rootView: View by bindView(R.id.rootView)
        val numberView: TextView by bindView(R.id.number)
        val contentView: TextView by bindView(R.id.content)
        val playButton: ImageView by bindView(R.id.play)
    }
}
