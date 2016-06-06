package kr.blogspot.crowjdh.inspirationgen.ui.adapters

import kr.blogspot.crowjdh.inspirationgen.extensions.insertOrUpdate
import kr.blogspot.crowjdh.inspirationgen.extensions.startActivity
import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength
import kr.blogspot.crowjdh.inspirationgen.music.models.Program
import kr.blogspot.crowjdh.inspirationgen.music.models.TimeSignature
import kr.blogspot.crowjdh.inspirationgen.ui.ScaleSettingsActivity
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.SETTINGS_TYPE_BAR
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.SETTINGS_TYPE_SHEET
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.SettingsType
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.VALUE_TYPE_CUSTOM
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.VALUE_TYPE_RADIO
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.VALUE_TYPE_VALUE
import kr.blogspot.crowjdh.inspirationgen.ui.adapters.SettingsAdapter.Settings.Companion.ValueType

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 15.
 *
 * GeneralSettingsAdapter
 */

class GeneralSettingsAdapter(): SettingsAdapter<GeneralSettingsAdapter.GeneralSettings>() {

    override fun getItemViewType(position: Int): Int {
        return GeneralSettings.values()[position].valueType.toInt()
    }

    override fun getItemCount() = GeneralSettings.values().count()

    override fun getItemAt(position: Int): GeneralSettings = GeneralSettings.values()[position]

    override fun getContents(item: GeneralSettings): String? {
        return when (item) {
            GeneralSettings.BPM -> sheetOptions.bpm.toString()
            GeneralSettings.TIME_SIGNATURE_COUNT -> barOptions.timeSignature.count.toString()
            GeneralSettings.BAR_COUNT -> barOptions.barCount.toString()
            GeneralSettings.TIME_SIGNATURE_NOTE_LENGTH -> barOptions.timeSignature.noteLength.name
            GeneralSettings.PROGRAM -> barOptions.program.title
            else -> null
        }
    }

    override fun getRadioTitles(item: GeneralSettings): Array<String>? {
        return when (item) {
            GeneralSettings.TIME_SIGNATURE_NOTE_LENGTH -> NoteLength.values().map { it.name }
            GeneralSettings.PROGRAM -> Program.values().map { it.title }
            else -> null
        }?.toTypedArray()
    }

    override fun getSelectedRadioIndex(item: GeneralSettings): Int? {
        return when (item) {
            GeneralSettings.TIME_SIGNATURE_NOTE_LENGTH -> barOptions.timeSignature.noteLength.ordinal
            GeneralSettings.PROGRAM -> barOptions.program.ordinal
            else -> null
        }
    }

    override fun onSelectRadio(item: GeneralSettings, index: Int) {
        when (item) {
            GeneralSettings.TIME_SIGNATURE_NOTE_LENGTH -> {
                barOptions.insertOrUpdate {
                    barOptions.timeSignature = TimeSignature(
                            barOptions.timeSignature.count,
                            NoteLength.values()[index])
                }
            }
            GeneralSettings.PROGRAM -> {
                barOptions.insertOrUpdate {
                    barOptions.program = Program.values()[index]
                }
            }
            else -> {}
        }
    }

    override fun onCustomAction(holder: SettingsViewHolder, item: GeneralSettings, index: Int) {
        when (item) {
            GeneralSettings.SCALE -> holder.rootView.context.startActivity(
                    ScaleSettingsActivity::class)
            else -> {}
        }
    }

    override fun insertOrUpdateOnValueChange(item: GeneralSettings, text: String) {
        return when (item) {
            GeneralSettingsAdapter.GeneralSettings.BPM -> sheetOptions.bpm = text.toInt()
            GeneralSettingsAdapter.GeneralSettings.TIME_SIGNATURE_COUNT -> {
                barOptions.timeSignature = TimeSignature(
                        text.toInt(), barOptions.timeSignature.noteLength)
            }
            GeneralSettingsAdapter.GeneralSettings.BAR_COUNT -> barOptions.barCount = text.toInt()
            else -> {}
        }
    }

    enum class GeneralSettings(override val title: String,
                               @ValueType override val valueType: Long,
                               @SettingsType override val settingsType: Long): SettingsAdapter.Settings {
        BPM("BPM", VALUE_TYPE_VALUE, SETTINGS_TYPE_SHEET),
        TIME_SIGNATURE_COUNT("Note Count Per Bar", VALUE_TYPE_VALUE, SETTINGS_TYPE_BAR),
        TIME_SIGNATURE_NOTE_LENGTH("Note Length Per Bar", VALUE_TYPE_RADIO, SETTINGS_TYPE_BAR),
        SCALE("Scale", VALUE_TYPE_CUSTOM, SETTINGS_TYPE_BAR),
        BAR_COUNT("Bar Count", VALUE_TYPE_VALUE, SETTINGS_TYPE_BAR),
        PROGRAM("Midi Program", VALUE_TYPE_RADIO, SETTINGS_TYPE_BAR);
    }

}
