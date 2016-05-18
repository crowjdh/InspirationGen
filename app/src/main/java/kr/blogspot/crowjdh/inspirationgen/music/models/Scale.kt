package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.inspirationgen.extensions.toPrefixSum

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 18.
 *
 * Scale
 */

class Scale(val pitches: List<Int>) {

    constructor(pitchRange: IntRange): this(pitchRange.toList())

    constructor(vararg pitches: Int): this(pitches.toList())

    companion object Factory {
        private val majorDiffs = arrayOf(0, 2, 2, 1, 2, 2, 2, 1)
        private val minorDiffs = arrayOf(0, 2, 1, 2, 2, 1, 2, 2)
        val C4 = 60

        val CHROMATIC = Scale(60..72)

        fun major(base: Int) = Scale(majorDiffs.toPrefixSum().map { it + base })
        fun minor(base: Int) = Scale(minorDiffs.toPrefixSum().map { it + base })
    }
}
