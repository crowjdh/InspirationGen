package kr.blogspot.crowjdh.inspirationgen.models

import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Bar
 */

class Bar(timeSignature: TimeSignature? = null): TickType {
    var timeSignature: TimeSignature
        get() {
            return _timeSignature ?: DEFAULT_TIME_SIGNATURE
        }
        set(value) {
            if (value.canContainTickType(this)) {
                _timeSignature = value
            }
        }

    private var notables: ArrayList<Notable> = arrayListOf()
    private var _timeSignature: TimeSignature? = null

    init {
        if (timeSignature != null) {
            this.timeSignature = timeSignature
        }
    }

    fun removeNotableAt(index: Int) = notables.removeAt(index)

    // TODO: Consider throwing exception when can't add notable
    fun addNotable(notable: Notable): Boolean {
        val canAddNotable = canAddNotable(notable)
        if (canAddNotable) {
            notables.add(notable)
        }
        return canAddNotable
    }

    override fun ticks(timeSignature: TimeSignature): Int =
            notables.map { it.length.ticks(timeSignature.tpqn) }
                    .fold(0) { prev, cur -> prev + cur }

    private fun canAddNotable(notable: Notable): Boolean {
        val targetTicks = ticks(timeSignature) + notable.length.ticks(timeSignature.tpqn)

        return targetTicks <= timeSignature.capableTicks()
    }
}
