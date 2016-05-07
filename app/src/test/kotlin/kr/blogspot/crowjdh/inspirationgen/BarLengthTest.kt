package kr.blogspot.crowjdh.inspirationgen

import kr.blogspot.crowjdh.inspirationgen.helpers.TestNotables
import kr.blogspot.crowjdh.inspirationgen.helpers.TestTimeSignatures
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.DEFAULT_TIME_SIGNATURE
import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength
import kr.blogspot.crowjdh.inspirationgen.music.models.TimeSignature
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 5.
 *
 * BarLengthTest
 */

class BarLengthTest {

    @Test
    fun createTimeSignature_checkCapableTicks() {
        TestTimeSignatures.forEachTimeSignatureParams { count, noteLength, tpqn ->
            assertCapableTicksForTimeSignature(count, noteLength, tpqn)
        }
    }

    @Test
    fun addNotables_defaultTimeSignature_doNotExceed() {
        TestNotables.permutateNotables { notables ->
            val bar = Bar()
            for (notable in notables) {
                bar.addNotableIgnoringResult(notable)
            }
            val barTicks = bar.ticks(DEFAULT_TIME_SIGNATURE)
            val capability = DEFAULT_TIME_SIGNATURE.capableTicks()
            assertTrue(barTicks <= capability)
        }
    }

    @Test
    fun changeTimeSignature_barWithDefaultTimeSignature_barTicksDoNotExceedTimeSignatureTicks() {
        TestNotables.permutateNotables { notables ->
            TestTimeSignatures.forEachTimeSignature { timeSignature ->
                val bar = Bar()
                for (notable in notables) {
                    if (!bar.addNotableAndGetResult(notable)) {
                        break
                    }
                }
                bar.timeSignature = timeSignature
                if (bar.timeSignature === timeSignature) {
                    assertTrue(bar.ticks(timeSignature) <= timeSignature.capableTicks())
                }
            }
        }
    }

    private fun assertCapableTicksForTimeSignature(count: Int, noteLength: NoteLength, tpqn: Int) {
        fun ticks(count: Int, noteLength: NoteLength, tpqn: Int): Int {
            val tickPerLength = tpqn * (NoteLength.QUARTER.length.toFloat() / noteLength.length.toFloat())
            return count * tickPerLength.toInt()
        }
        assertEquals(TimeSignature(count, noteLength, tpqn).capableTicks(),
                ticks(count, noteLength, tpqn))
    }
}
