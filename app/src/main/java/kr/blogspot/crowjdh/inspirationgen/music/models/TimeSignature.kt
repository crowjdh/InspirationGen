package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * TimeSignature
 */

data class TimeSignature(val count: Int, val noteLength: NoteLength) {

    fun capableTicks() = noteLength.ticks() * count
    fun canContainTickType(tickType: TickType) = tickType.ticks <= capableTicks()

    companion object Factory {
        fun createDefault() = TimeSignature(4, NoteLength.QUARTER)
    }
}
