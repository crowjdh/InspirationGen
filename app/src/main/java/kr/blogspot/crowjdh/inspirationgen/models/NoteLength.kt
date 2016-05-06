package kr.blogspot.crowjdh.inspirationgen.models

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

    fun ticks(tpqn: Int) = (tpqn * (QUARTER.length.toFloat() / length.toFloat())).toInt()
}
