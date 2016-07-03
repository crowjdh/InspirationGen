package kr.blogspot.crowjdh.inspirationgen.ui.adapters

import android.app.AlertDialog
import android.support.annotation.IntDef
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.RxTextView
import kr.blogspot.crowjdh.inspirationgen.R
import kr.blogspot.crowjdh.inspirationgen.extensions.all
import kr.blogspot.crowjdh.inspirationgen.extensions.database
import kr.blogspot.crowjdh.inspirationgen.extensions.firstOrDefault
import kr.blogspot.crowjdh.inspirationgen.extensions.insertOrUpdate
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.MasterSettings
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import rx.Subscription

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 6. 6.
 *
 * SettingsAdapter
 */

abstract class SettingsAdapter<T: SettingsAdapter.Settings>():
        RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    protected val sheetOptions
            = database.all<Sheet.Options>(Sheet.Options::class)
            .firstOrDefault(Sheet.Options.default)
    protected val barOptions
            = database.all<Bar.Generator.Options>(Bar.Generator.Options::class)
            .firstOrDefault(Bar.Generator.Options.default)
    protected val masterSettings
            = database.all<MasterSettings>(MasterSettings::class)
            .firstOrDefault(MasterSettings.default)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingsAdapter.SettingsViewHolder? {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.vh_setting, parent, false)
        return SettingsAdapter.SettingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsAdapter.SettingsViewHolder?, position: Int) {
        unSubscribeAllFromViewHolder(holder!!)

        val item = getItemAt(position)

        showProperViews(holder, item)
        fillContents(holder, item)
        setActions(holder, item, position)
        observeValueChanges(holder, item)
    }

    override fun onViewRecycled(holder: SettingsAdapter.SettingsViewHolder?) {
        super.onViewRecycled(holder)
        unSubscribeAllFromViewHolder(holder!!)
    }

    private fun unSubscribeAllFromViewHolder(holder: SettingsAdapter.SettingsViewHolder) {
        holder.subscriptions.forEach { it.unsubscribe() }
    }

    private fun showProperViews(holder: SettingsViewHolder, item: T) {
        when (item.valueType) {
            Settings.VALUE_TYPE_VALUE -> {
                holder.valueEditText.visibility = View.VISIBLE
                holder.valueTextView.visibility = View.GONE
            }
            Settings.VALUE_TYPE_RADIO -> {
                holder.valueEditText.visibility = View.GONE
                holder.valueTextView.visibility = View.VISIBLE
            }
            Settings.VALUE_TYPE_CUSTOM -> {
                holder.valueEditText.visibility = View.GONE
                holder.valueTextView.visibility = View.GONE
            }
            else -> throw UnsupportedOperationException(
                    "valueType ${item.valueType} not supported yet.")
        }
    }

    private fun fillContents(holder: SettingsViewHolder, item: T) {
        holder.titleView.text = item.title
        holder.valueEditText.setText("")
        holder.valueTextView.text = ""
        when (item.valueType) {
            Settings.VALUE_TYPE_VALUE -> holder.valueEditText.setText(getContents(item))
            Settings.VALUE_TYPE_RADIO -> holder.valueTextView.text = getContents(item)
            Settings.VALUE_TYPE_CUSTOM -> {}
            else -> throw UnsupportedOperationException(
                    "valueType ${item.valueType} not supported yet.")
        }
    }

    private fun setActions(holder: SettingsViewHolder, item: T, position: Int) {
        if (item.valueType == Settings.VALUE_TYPE_RADIO) {
            holder.rootView.clicks().subscribe {
                val titles = getRadioTitles(item)
                val index = getSelectedRadioIndex(item)

                if (titles == null || index == null) {
                    return@subscribe
                }
                AlertDialog.Builder(holder.titleView.context)
                        .setTitle(item.title)
                        .setSingleChoiceItems(titles, index, { dialog, i ->
                            val closeDialog = onSelectRadio(item, i)
                            notifyItemChanged(position)
                            if (closeDialog) {
                                dialog.dismiss()
                            }
                        }).show()
            }
        } else if (item.valueType == Settings.VALUE_TYPE_CUSTOM) {
            holder.rootView.clicks().subscribe {
                onCustomAction(holder, item, position)
            }
        } else {
            holder.rootView.clicks().subscribe {  }
        }
    }

    private fun observeValueChanges(holder: SettingsViewHolder, item: T) {
        val afterTextSubs = RxTextView.afterTextChangeEvents(holder.valueEditText)
                .filter { it.editable().length > 0 }
                .map { it.editable().toString() }
                .subscribe { text ->
                    when (item.settingsType) {
                        Settings.SETTINGS_TYPE_SHEET -> sheetOptions.insertOrUpdate {
                            insertOrUpdateOnValueChange(item, text)
                        }
                        Settings.SETTINGS_TYPE_BAR -> barOptions.insertOrUpdate {
                            insertOrUpdateOnValueChange(item, text)
                        }
                        Settings.SETTINGS_TYPE_MASTER -> masterSettings.insertOrUpdate {
                            insertOrUpdateOnValueChange(item, text)
                        }
                        else -> throw UnsupportedOperationException(
                                "settingsType ${item.settingsType} not supported yet.")
                    }
                }
        holder.subscriptions.add(afterTextSubs)
    }

    abstract fun getItemAt(position: Int): T
    abstract fun getContents(item: T): String?
    abstract fun getRadioTitles(item: T): Array<String>?
    abstract fun getSelectedRadioIndex(item: T): Int?
    /**
     * Perform and return true to close radio dialog.
     */
    abstract fun onSelectRadio(item: T, index: Int): Boolean
    abstract fun onCustomAction(holder: SettingsViewHolder, item: T, index: Int)
    abstract fun insertOrUpdateOnValueChange(item: T, text: String)

    class SettingsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val rootView: View by bindView(R.id.root_view)
        val titleView: TextView by bindView(R.id.number)
        val valueEditText: EditText by bindView(R.id.value_edit_text)
        val valueTextView: TextView by bindView(R.id.value_text_view)

        var subscriptions: MutableList<Subscription> = mutableListOf()
    }

    interface Settings {

        val title: String
        val valueType: Long
        val settingsType: Long

        companion object {

            const val VALUE_TYPE_VALUE = 0L
            const val VALUE_TYPE_RADIO = 1L
            const val VALUE_TYPE_CUSTOM = 2L

            const val SETTINGS_TYPE_SHEET = 0L
            const val SETTINGS_TYPE_BAR = 1L
            const val SETTINGS_TYPE_MASTER = 2L

            @IntDef(VALUE_TYPE_VALUE, VALUE_TYPE_RADIO, VALUE_TYPE_CUSTOM)
            @Target(AnnotationTarget.VALUE_PARAMETER)
            @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
            annotation class ValueType

            @IntDef(SETTINGS_TYPE_SHEET, SETTINGS_TYPE_BAR, SETTINGS_TYPE_MASTER)
            @Target(AnnotationTarget.VALUE_PARAMETER)
            @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
            annotation class SettingsType
        }
    }
}