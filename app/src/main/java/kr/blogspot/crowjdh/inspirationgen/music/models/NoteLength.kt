package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * NoteLength
 *  Length of Notable.
 */

enum class NoteLength(val length: Int) {
    WHOLE(1),
    HALF(2),
    QUARTER(4),
    EIGHTH(8),
    SIXTEENTH(16);

    fun ticks() = (DEFAULT_TPQN * (QUARTER.length.toFloat() / length.toFloat())).toLong()

    companion object Factory {

        fun fromTicks(ticksToFill: Long): List<NoteLength>? {
            if (ticksToFill == 0L) {
                return null
            }

            val noteLengths = mutableListOf<NoteLength>()
            var leftTicksToFill = ticksToFill
            do {
                // ticks = tpqn * (QUARTER.length / length)
                // QUARTER.length / length = ticks / tpqn
                // length = QUARTER.length / (ticks / tpqn) = QUARTER.length * (tpqn / ticks)
                val length = QUARTER.length * DEFAULT_TPQN.toFloat() / leftTicksToFill.toFloat()
                val noteLength = fromLength(length) ?: break
                noteLengths.add(noteLength)
                leftTicksToFill -= noteLength.ticks()
            } while(leftTicksToFill > 0)
            return noteLengths
        }

        fun fromLength(length: Float)= values().filterOrNull { it.length >= length }?.get(0)

        inline fun <T> Array<out T>.filterOrNull(predicate: (T) -> Boolean): List<T>? {
            val filtered = filter(predicate)
            return if (filtered.count() > 0) filtered else null
        }
    }
}
