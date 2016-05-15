package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.inspirationgen.helpers.generateRandomName
import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Sheet
 */

const val DEFAULT_TPQN = 480
class Sheet() {

    private var _bars: ArrayList<Bar> = arrayListOf()
    var name: String = generateRandomName()
    val bars: ArrayList<Bar>
        get() = _bars

    fun addBar(bar: Bar) = _bars.add(bar)

    fun addBars(bars: List<Bar>) = _bars.addAll(bars)

    fun removeBarAt(index: Int) = _bars.removeAt(index)
}
