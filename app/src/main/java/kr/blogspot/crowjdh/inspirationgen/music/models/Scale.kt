package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.inspirationgen.extensions.hashCodeWith
import kr.blogspot.crowjdh.inspirationgen.extensions.toPrefixSum

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 18.
 *
 * Scale
 */

// C3
private const val BASE_C = 48

class Scale(val key: Key,
            val intervals: Intervals) {

    companion object {
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
        C("C", BASE_C), C_SHARP("C#", BASE_C + 1),
        D("D", BASE_C + 2), D_SHARP("D#", BASE_C + 3),
        E("E", BASE_C + 4),
        F("F", BASE_C + 5), F_SHARP("F#", BASE_C + 6),
        G("G", BASE_C + 7), G_SHARP("G#", BASE_C + 8),
        A("A", BASE_C + 9), A_SHARP("A#", BASE_C + 10),
        B("B", BASE_C + 11)
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
