package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 7. 3.
 *
 * MasterSettings
 */

data class MasterSettings(var enableClickTrack: Boolean = false): Record {
    override var _id: Long = Record.invalidId
    override val records: List<Record>? = null

    companion object Factory {
        val default = create {}

        fun create(build: MasterSettings.() -> Unit): MasterSettings {
            val options = MasterSettings()
            options.build()
            return options
        }
    }
}
