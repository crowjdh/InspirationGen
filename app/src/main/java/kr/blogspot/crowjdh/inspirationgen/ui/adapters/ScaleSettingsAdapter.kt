package kr.blogspot.crowjdh.inspirationgen.ui.adapters

import kr.blogspot.crowjdh.inspirationgen.extensions.insertOrUpdate
import kr.blogspot.crowjdh.inspirationgen.music.models.Scale
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.SETTINGS_TYPE_BAR
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.SettingsType
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.VALUE_TYPE_RADIO
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.ValueType

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 15.
 *
 * ScaleSettingsAdapter
 */

class ScaleSettingsAdapter(): SettingsAdapter<ScaleSettingsAdapter.ScaleSettings>() {

    override fun getItemViewType(position: Int): Int {
        return ScaleSettings.values()[position].valueType.toInt()
    }

    override fun getItemCount() = ScaleSettings.values().count()

    override fun getItemAt(position: Int): ScaleSettings = ScaleSettings.values()[position]

    override fun getContents(item: ScaleSettings): String? {
        return when (item) {
            ScaleSettings.KEY -> barOptions.scale.key.displayName
            ScaleSettings.INTERVALS -> barOptions.scale.intervals.displayName
            else -> null
        }
    }

    override fun getRadioTitles(item: ScaleSettings): Array<String>? {
        return when (item) {
            ScaleSettings.KEY -> Scale.Key.values().map { it.displayName }
            ScaleSettings.INTERVALS -> Scale.Intervals.values().map { it.displayName }
            else -> null
        }?.toTypedArray()
    }

    override fun getSelectedRadioIndex(item: ScaleSettings): Int? {
        return when (item) {
            ScaleSettings.KEY -> barOptions.scale.key.ordinal
            ScaleSettings.INTERVALS -> barOptions.scale.intervals.ordinal
            else -> null
        }
    }

    override fun onSelectRadio(item: ScaleSettings, index: Int) {
        return when (item) {
            ScaleSettings.KEY -> {
                barOptions.insertOrUpdate {
                    barOptions.scale = Scale(Scale.Key.values()[index], barOptions.scale.intervals)
                }
            }
            ScaleSettings.INTERVALS -> {
                barOptions.insertOrUpdate {
                    barOptions.scale = Scale(barOptions.scale.key, Scale.Intervals.values()[index])
                }
            }
            else -> {}
        }
    }

    override fun insertOrUpdateOnValueChange(item: ScaleSettings, text: String) {
        // No value setting items
    }

    override fun onCustomAction(holder: SettingsViewHolder, item: ScaleSettings, index: Int) {
        // No custom setting items
    }

    enum class ScaleSettings(override val title: String,
                               @ValueType override val valueType: Long,
                               @SettingsType override val settingsType: Long): SettingsAdapter.Settings {
        KEY("Key", VALUE_TYPE_RADIO, SETTINGS_TYPE_BAR),
        INTERVALS("Intervals", VALUE_TYPE_RADIO, SETTINGS_TYPE_BAR)
    }

}
