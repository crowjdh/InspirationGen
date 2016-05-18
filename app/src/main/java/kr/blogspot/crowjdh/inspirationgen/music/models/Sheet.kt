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
            val bars: ArrayList<Bar> = arrayListOf()): Record {

    override val records: List<Record>
        get() = bars

    override fun hashCode(): Int {
        return hashCodeWith(_id, name, bars)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Sheet) {
            return false
        }
        return other._id.equals(this._id)
                && other.name.equals(this.name)
                && other.bars.equals(this.bars)
    }

}
