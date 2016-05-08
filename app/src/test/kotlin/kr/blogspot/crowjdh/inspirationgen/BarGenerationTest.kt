package kr.blogspot.crowjdh.inspirationgen

import kr.blogspot.crowjdh.inspirationgen.helpers.TestNotables
import kr.blogspot.crowjdh.inspirationgen.helpers.TestTimeSignatures
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 7.
 *
 * BarGenerationTest
 */

class BarGenerationTest {

    @Test
    fun createNoteLength_barWithFullTicks_shouldReturnNull() {
        TestNotables.permutateNotables { notables ->
            TestTimeSignatures.forEachTimeSignature { timeSignature ->
                val bar = Bar()
                for (notable in notables) {
                    if (!bar.addNotableAndGetResult(notable)) {
                        break
                    }
                }
                val noteLengthsToFill =
                        NoteLength.fromTicks(bar.ticksLeft)
                if (noteLengthsToFill != null) {
                    val ticksOfNoteLengthsToFill = noteLengthsToFill
                            .map { it.ticks() }
                            .reduce { prev, cur -> prev + cur }
                    assertEquals(ticksOfNoteLengthsToFill, bar.ticksLeft,
                            "bar.ticksLeft: ${bar.ticksLeft}, " +
                                    "ticksOfNoteLengthsToFill: $ticksOfNoteLengthsToFill")
                } else {
                    assertEquals(bar.timeSignature.capableTicks(), bar.ticks)
                }
            }
        }
    }

    @Test
    fun createBar_seedFromZeroToOneHundred_shouldNotHaveLeftTicks() {
        for (i in 0..100) {
            val bar = Bar.generate { fixedSeed = i.toLong() }

            assertEquals(bar.ticksLeft, 0, "bar.ticks(): ${bar.ticks}, " +
                    "bar.ticksLeft: ${bar.ticksLeft}")
        }
    }
}