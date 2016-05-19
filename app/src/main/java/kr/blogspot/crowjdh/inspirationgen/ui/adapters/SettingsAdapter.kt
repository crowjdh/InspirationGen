package kr.blogspot.crowjdh.inspirationgen.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.widget.RxTextView
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.extensions.all
import kr.blogspot.crowjdh.inspirationgen.extensions.database
import kr.blogspot.crowjdh.inspirationgen.extensions.insertOrUpdate
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import kr.blogspot.crowjdh.inspirationgen.music.models.TimeSignature
import rx.Subscription

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 15.
 *
 * SettingsAdapter
 */

class SettingsAdapter(): RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    private val sheetOptions
            = database.all<Sheet.Options>(Sheet.Options::class).firstOrNull()
            ?: Sheet.Options.default
    private val barOptions
            = database.all<Bar.Generator.Options>(Bar.Generator.Options::class).firstOrNull()
            ?: Bar.Generator.Options.default

    interface OnItemClickListener {
        fun onItemClick(index: Int, item: Sheet)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingsViewHolder? {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.vh_setting, parent, false)
        return SettingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder?, position: Int) {
        unSubscribeAllFromViewHolder(holder!!)

        val item = Settings.values()[position]

        showProperViews(holder, item)
        applyItem(holder, item)
        observeValueChanges(holder, item)
    }

    override fun onViewRecycled(holder: SettingsViewHolder?) {
        super.onViewRecycled(holder)
        unSubscribeAllFromViewHolder(holder!!)
    }

    override fun getItemViewType(position: Int): Int {
        return Settings.values()[position].type
    }

    override fun getItemCount() = Settings.values().count()

    private fun unSubscribeAllFromViewHolder(holder: SettingsViewHolder) {
        holder.subscriptions.forEach { it.unsubscribe() }
    }

    private fun applyItem(holder: SettingsViewHolder, item: Settings) {
        holder.titleView.text = item.name
        holder.valueView.setText(when (item) {
            Settings.BPM -> sheetOptions.bpm
            Settings.TIME_SIGNATURE_COUNT -> barOptions.timeSignature.count
            Settings.BAR_COUNT -> barOptions.barCount
            else -> null
        }?.toString())
    }

    private fun showProperViews(holder: SettingsViewHolder, item: Settings) {
        holder.valueView.visibility = when (item.type) {
            0 -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun observeValueChanges(holder: SettingsViewHolder, item: Settings) {
        val afterTextSubs = RxTextView.afterTextChangeEvents(holder.valueView)
                .filter { it.editable().length > 0 }
                .map { it.editable().toString() }
                .subscribe { text ->
                    val block: () -> Unit = when (item) {
                        Settings.BPM -> {{
                            sheetOptions.bpm = text.toInt()
                        }}
                        Settings.TIME_SIGNATURE_COUNT -> {{
                            barOptions.timeSignature = TimeSignature(
                                    text.toInt(), barOptions.timeSignature.noteLength)
                        }}
                        Settings.BAR_COUNT -> {{ barOptions.barCount = text.toInt() }}
                        else -> {{}}
                    }
                    when (item) {
                        Settings.BPM -> sheetOptions.insertOrUpdate { block() }
                        else -> barOptions.insertOrUpdate { block() }
                    }
                }
        holder.subscriptions.add(afterTextSubs)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    class SettingsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val titleView: TextView by bindView(R.id.number)
        val valueView: EditText by bindView(R.id.value)
        var subscriptions: MutableList<Subscription> = mutableListOf()
    }

    enum class Settings(val title: String, val type: Int) {
        BPM("BPM", 0),
        TIME_SIGNATURE_COUNT("Note Count Per Bar", 0),
        TIME_SIGNATURE_NOTE_LENGTH("Note Length Per Bar", 1),
        SCALE("Scale", 1),
        BAR_COUNT("Bar Count", 0)
    }

}
