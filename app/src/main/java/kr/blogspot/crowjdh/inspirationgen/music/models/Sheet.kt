package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.inspirationgen.extensions.hashCodeWith
import kr.blogspot.crowjdh.inspirationgen.helpers.generateRandomName
import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Sheet
 */

const val DEFAULT_TPQN = 480
class Sheet(override var _id: Long = Record.invalidId,
            var name: String = generateRandomName(),
            val bars: ArrayList<Bar> = arrayListOf(),
            var pinned: Boolean = false,
            options: Options): Record {

    constructor(_id: Long = Record.invalidId,
                name: String = generateRandomName(),
                bars: ArrayList<Bar> = arrayListOf(),
                pinned: Boolean = false,
                optionBuilder: Options.() -> Unit = {}): this(
            _id, name, bars, pinned, Options.create(optionBuilder))

    var bpm: Int = options.bpm

    override val records: List<Record>
        get() = bars

    override fun hashCode(): Int {
        return hashCodeWith(_id, name, bars, pinned)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Sheet) {
            return false
        }
        return other._id.equals(this._id)
                && other.name.equals(this.name)
                && other.bars.equals(this.bars)
                && other.pinned.equals(this.pinned)
    }

    class Options(var bpm: Int = 120): Record {

        override var _id: Long = Record.invalidId
        override val records = null

        override fun hashCode(): Int {
            return hashCodeWith(_id, bpm)
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Options) {
                return false
            }
            return other._id.equals(this._id)
                    && bpm.equals(bpm)
        }

        fun validateOrThrow() {
            assert(bpm > 0) { "bpm MUST BE > 0" }
        }

        companion object Factory {

            val default = create { }

            fun create(build: Options.() -> Unit): Options {
                val options = Options()
                options.build()
                options.validateOrThrow()
                return options
            }
        }
    }

}
