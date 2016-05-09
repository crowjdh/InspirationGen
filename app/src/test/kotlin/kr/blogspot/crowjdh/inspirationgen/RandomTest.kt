package kr.blogspot.crowjdh.inspirationgen

import kr.blogspot.crowjdh.inspirationgen.extensions.pickFromMap
import kr.blogspot.crowjdh.inspirationgen.helpers.printSection
import org.junit.BeforeClass
import org.junit.Test
import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 9.
 *
 * RandomTest
 */

class RandomTest {

    private val SEED = 100L

    companion object {

        lateinit var ZERO_BIAS_SOURCE_MAP: Map<Char, Int>
        lateinit var SOURCE_MAP: Map<Char, Int>

        @BeforeClass
        @JvmStatic
        @Suppress("unused")
        fun setUpBeforeClass() {
            val zeroBiasSourceMap = mutableMapOf<Char, Int>()
            val sourceMap = mutableMapOf<Char, Int>()
            for (idx in 0..10) {
                zeroBiasSourceMap['a' + idx] = 0
                sourceMap['a' + idx] = idx
            }
            ZERO_BIAS_SOURCE_MAP = zeroBiasSourceMap
            SOURCE_MAP = sourceMap
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun performPick_zeroBiasValues_throwException() {
        Random(SEED).pickFromMap(ZERO_BIAS_SOURCE_MAP)
    }

    @Test
    fun performPickAndPrintResult() {
        printSection("SOURCE_MAP: ${SOURCE_MAP.toString()}")
        val random = Random(SEED)
        for (idx in 0..10) {
            val result = random.pickFromMap(SOURCE_MAP)
            println("result: $result")
        }
    }
}
