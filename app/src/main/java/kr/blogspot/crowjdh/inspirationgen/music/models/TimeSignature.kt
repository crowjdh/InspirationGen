package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * TimeSignature
 */

class TimeSignature(val count: Int, val noteLength: NoteLength, val tpqn: Int) {
    fun capableTicks() = noteLength.ticks(tpqn) * count
    fun canContainTickType(tickType: TickType) = tickType.ticks(this) <= capableTicks()
}
