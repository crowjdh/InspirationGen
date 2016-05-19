package kr.blogspot.crowjdh.inspirationgen.ui.adapters

import android.support.annotation.IntDef
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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingsViewHolder? {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.vh_setting, parent, false)
        return SettingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder?, position: Int) {
        unSubscribeAllFromViewHolder(holder!!)

        val item = Settings.values()[position]

        showProperViews(holder, item)
        fillContents(holder, item)
        observeValueChanges(holder, item)
    }

    override fun onViewRecycled(holder: SettingsViewHolder?) {
        super.onViewRecycled(holder)
        unSubscribeAllFromViewHolder(holder!!)
    }

    override fun getItemViewType(position: Int): Int {
        return Settings.values()[position].valueType.toInt()
    }

    override fun getItemCount() = Settings.values().count()

    private fun unSubscribeAllFromViewHolder(holder: SettingsViewHolder) {
        holder.subscriptions.forEach { it.unsubscribe() }
    }

    private fun showProperViews(holder: SettingsViewHolder, item: Settings) {
        when (item.valueType) {
            Settings.VALUE_TYPE_VALUE -> {
                holder.valueView.visibility = View.VISIBLE
            }
            Settings.VALUE_TYPE_RADIO -> {
                holder.valueView.visibility = View.GONE
            }
            else -> throw UnsupportedOperationException(
                    "valueType ${item.valueType} not supported yet.")
        }
    }

    private fun fillContents(holder: SettingsViewHolder, item: Settings) {
        holder.titleView.text = item.title
        holder.valueView.setText(when (item) {
            Settings.BPM -> sheetOptions.bpm
            Settings.TIME_SIGNATURE_COUNT -> barOptions.timeSignature.count
            Settings.BAR_COUNT -> barOptions.barCount
            else -> null
        }?.toString())
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
                    when (item.settingsType) {
                        Settings.SETTINGS_TYPE_SHEET -> sheetOptions.insertOrUpdate { block() }
                        Settings.SETTINGS_TYPE_BAR -> barOptions.insertOrUpdate { block() }
                        else -> throw UnsupportedOperationException(
                                "settingsType ${item.settingsType} not supported yet.")
                    }
                }
        holder.subscriptions.add(afterTextSubs)
    }

    class SettingsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val titleView: TextView by bindView(R.id.number)
        val valueView: EditText by bindView(R.id.value)
        var subscriptions: MutableList<Subscription> = mutableListOf()
    }

    enum class Settings(val title: String,
                        @ValueType val valueType: Long,
                        @SettingsType val settingsType: Long) {
        BPM("BPM", VALUE_TYPE_VALUE, SETTINGS_TYPE_SHEET),
        TIME_SIGNATURE_COUNT("Note Count Per Bar", VALUE_TYPE_VALUE, SETTINGS_TYPE_BAR),
        TIME_SIGNATURE_NOTE_LENGTH("Note Length Per Bar", VALUE_TYPE_RADIO, SETTINGS_TYPE_BAR),
        SCALE("Scale", VALUE_TYPE_RADIO, SETTINGS_TYPE_BAR),
        BAR_COUNT("Bar Count", VALUE_TYPE_VALUE, SETTINGS_TYPE_BAR);

        companion object {

            const val VALUE_TYPE_VALUE = 0L
            const val VALUE_TYPE_RADIO = 1L

            const val SETTINGS_TYPE_SHEET = 0L
            const val SETTINGS_TYPE_BAR = 1L

            @IntDef(VALUE_TYPE_VALUE, VALUE_TYPE_RADIO)
            @Target(AnnotationTarget.VALUE_PARAMETER)
            @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
            annotation class ValueType

            @IntDef(SETTINGS_TYPE_SHEET, SETTINGS_TYPE_BAR)
            @Target(AnnotationTarget.VALUE_PARAMETER)
            @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
            annotation class SettingsType
        }
    }

}
