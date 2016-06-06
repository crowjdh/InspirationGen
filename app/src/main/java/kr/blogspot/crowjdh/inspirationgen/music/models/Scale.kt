package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.inspirationgen.extensions.hashCodeWith
import kr.blogspot.crowjdh.inspirationgen.extensions.toPrefixSum

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 18.
 *
 * Scale
 */

private const val PITCH_C4 = 60

class Scale(val key: Key,
            val intervals: Intervals) {

    companion object Factory {
        val default = Scale(Key.C, Intervals.MAJOR)
    }

    val pitches = intervals.intervalArray.toPrefixSum().map { it + key.root }

    override fun hashCode() = hashCodeWith(key, intervals)

    override fun equals(other: Any?): Boolean {
        if (other !is Scale) {
            return false
        }
        return other.key.equals(this.key)
                && other.intervals.equals(this.intervals)
    }

    enum class Key(val displayName: String, val root: Int) {
        C("C", PITCH_C4), C_SHARP("C#", PITCH_C4 + 1),
        D("D", PITCH_C4 + 2), D_SHARP("D#", PITCH_C4 + 3),
        E("E", PITCH_C4 + 4),
        F("F", PITCH_C4 + 5), F_SHARP("F#", PITCH_C4 + 6),
        G("G", PITCH_C4 + 7), G_SHARP("G#", PITCH_C4 + 8),
        A("A", PITCH_C4 + 9), A_SHARP("A#", PITCH_C4 + 10),
        B("B", PITCH_C4 + 11)
    }

    enum class Intervals(val displayName: String, vararg intervals: Int) {
        MAJOR("Major", 0, 2, 2, 1, 2, 2, 2, 1),
        MINOR("Minor", 0, 2, 1, 2, 2, 1, 2, 2),
        MAJOR_PENTATONIC("Major Pentatonic", 0, 2, 2, 3, 2, 3),
        MINOR_PENTATONIC("Minor Pentatonic", 0, 3, 2, 2, 3, 2),
        CHROMATIC("Chromatic", 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

        val intervalArray = intervals.toTypedArray()
    }
}
