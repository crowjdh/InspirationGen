package kr.blogspot.crowjdh.inspirationgen

import kr.blogspot.crowjdh.inspirationgen.helpers.TestNotables
import kr.blogspot.crowjdh.inspirationgen.helpers.TestTimeSignatures
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.DEFAULT_TPQN
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
        TestTimeSignatures.forEachTimeSignatureParams { count, noteLength ->
            assertCapableTicksForTimeSignature(count, noteLength)
        }
    }

    @Test
    fun addNotables_defaultTimeSignature_doNotExceed() {
        TestNotables.permutateNotables { notables ->
            val bar = Bar()
            for (notable in notables) {
                bar.addNotableIgnoringResult(notable)
            }
            val barTicks = bar.ticks
            val capability = TimeSignature.default.capableTicks()
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
                    assertTrue(bar.ticks <= timeSignature.capableTicks())
                }
            }
        }
    }

    private fun assertCapableTicksForTimeSignature(count: Int, noteLength: NoteLength) {
        fun ticks(count: Int, noteLength: NoteLength): Long {
            val tickPerLength = DEFAULT_TPQN *
                    (NoteLength.QUARTER.length.toFloat() / noteLength.length.toFloat())
            return count * tickPerLength.toLong()
        }
        assertEquals(TimeSignature(count, noteLength).capableTicks(),
                ticks(count, noteLength))
    }
}
