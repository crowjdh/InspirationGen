package kr.blogspot.crowjdh.inspirationgen

import kr.blogspot.crowjdh.inspirationgen.extensions.increaseOrPut
import kr.blogspot.crowjdh.inspirationgen.helpers.TestNotables
import kr.blogspot.crowjdh.inspirationgen.helpers.TestTimeSignatures
import kr.blogspot.crowjdh.inspirationgen.helpers.printSection
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.Note
import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength
import kr.blogspot.crowjdh.inspirationgen.music.models.Rest
import org.junit.Test
import java.util.*
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
                    if (!bar.notables.add(notable)) {
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
            val bar = Bar.generate { fixedSeed = i.toLong() }[0]

            assertEquals(bar.ticksLeft, 0, "bar.ticks(): ${bar.ticks}, " +
                    "bar.ticksLeft: ${bar.ticksLeft}")
        }
    }

    @Test
    fun generateOneThousandBars_printBias() {
        generateBarsAndPrintResult(10000)
        for (i in 1..10) {
            println("\n\n\n\n")
            generateBarsAndPrintResult(1)
        }
    }

    fun generateBarsAndPrintResult(barCount: Int) {
        var noteCount = 0
        var restCount = 0
        var pitchMap = mutableMapOf<Int, Int>()
        var lengthMap = mutableMapOf<NoteLength, Int>()

        // Generate random bars and collect information
        Bar.generate {
            this.barCount = barCount
            this.noteOverRestBias = .8f
            this.noteLengthRange = Bar.Generator.NoteLengthRange.create(
                    Pair(NoteLength.QUARTER, 20), Pair(NoteLength.EIGHTH, 80))
        }.flatMap { it.notables }.forEach {
            if (it is Note) {
                noteCount++
                pitchMap.increaseOrPut(it.pitch, { 0 })
            } else if (it is Rest) {
                restCount++
            }
            lengthMap.increaseOrPut(it.length, { 0 })
        }

        val notableCount = noteCount + restCount

        printSection("Generated $barCount bar(s)", 0)

        // Print Notes vs Rests
        val notePercentage = (noteCount.toFloat() / notableCount.toFloat()) * 100
        printSection("Note vs Rest")
        println("Note: $notePercentage% ($noteCount)")
        println("Rest: ${(100 - notePercentage)}% ($restCount)")

        // Print pitch statistics
        printSection("Pitch")
        pitchMap.toSortedMap(Comparator { prev, cur -> prev - cur }).forEach {
            println("Pitch ${it.key}: ${100 * it.value.toFloat() / noteCount.toFloat()}% (${it.value})")
        }
        printSection("NoteLength")
        lengthMap.toSortedMap(Comparator { prev, cur -> prev.ordinal - cur.ordinal }).forEach {
            println("${it.key} length notes: ${100 * it.value.toFloat() / notableCount.toFloat()}% (${it.value})")
        }
    }
}
