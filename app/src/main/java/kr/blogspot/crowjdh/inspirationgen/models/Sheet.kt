package kr.blogspot.crowjdh.inspirationgen.models

import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Sheet
 *  description
 */

val DEFAULT_TIME_SIGNATURE = TimeSignature(4, NoteLength.QUARTER, 480)
class Sheet(var timeSignature: TimeSignature = DEFAULT_TIME_SIGNATURE) {
    private var bars: ArrayList<Bar> = arrayListOf()

    fun addBar(bar: Bar) = bars.add(bar)

    fun removeBarAt(index: Int) = bars.removeAt(index)
}
