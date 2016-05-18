package kr.blogspot.crowjdh.inspirationgen.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.bindView
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 15.
 *
 * SettingsAdapter
 */

class SettingsAdapter(): RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(index: Int, item: Sheet)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingsViewHolder? {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.vh_setting, parent, false)
        return SettingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder?, position: Int) {
        val item = Settings.values()[position]

        holder!!.valueView.visibility = when (item.type) {
            0 -> View.VISIBLE
            else -> View.GONE
        }

        holder.numberView.text = item.name
//        RxTextView.text(holder.valueView).
//        holder.valueView.text.
    }

    override fun getItemViewType(position: Int): Int {
        return Settings.values()[position].type
    }

    override fun getItemCount() = Settings.values().count()

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    class SettingsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val numberView: TextView by bindView(R.id.number)
        val valueView: EditText by bindView(R.id.value)
    }

    enum class Settings(val title: String, val type: Int) {
        BPM("BPM", 0),
        TIME_SIGNATURE_COUNT("Note Count Per Bar", 0),
        TIME_SIGNATURE_NOTE_LENGTH("Note Length Per Bar", 1),
        SCALE("Scale", 1),
        BAR_COUNT("Bar Count", 0)
    }

}
