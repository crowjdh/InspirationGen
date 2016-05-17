package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.inspirationgen.helpers.generateRandomName
import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Sheet
 */

const val DEFAULT_TPQN = 480
class Sheet(override var _id: Long? = null,
            var name: String = generateRandomName(),
            private val bars: ArrayList<Bar> = arrayListOf()): Record {

    fun addBar(bar: Bar) = bars.add(bar)
    fun addBars(bars: List<Bar>) = this.bars.addAll(bars)
    fun removeBarAt(index: Int) = bars.removeAt(index)
    fun forEachBars(action: (Bar) -> Unit): Unit {
        for (element in bars) action(element)
    }
}
