package kr.blogspot.crowjdh.inspirationgen

import com.google.common.collect.Collections2
import kr.blogspot.crowjdh.inspirationgen.models.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 5.
 *
 * BarLengthTest
 *  description
 */

class BarLengthTest {

    @Test
    fun createTimeSignature_checkCapableTicks() {
        forEachTimeSignatureParams { count, noteLength, tpqn ->
            assertCapableTicksForTimeSignature(count, noteLength, tpqn)
        }
    }

    @Test
    fun addNotables_defaultTimeSignature_doNotExceed() {
        permutateNotables { notables ->
            val bar = Bar()
            for (notable in notables) {
                bar.addNotable(notable)
            }
            val barTicks = bar.ticks(DEFAULT_TIME_SIGNATURE)
            val capability = DEFAULT_TIME_SIGNATURE.capableTicks()
            assertTrue(barTicks <= capability)
        }
    }

    @Test
    fun changeTimeSignature_barWithDefaultTimeSignature_barTicksDoNotExceedTimeSignatureTicks() {
        permutateNotables { notables ->
            forEachTimeSignature { timeSignature ->
                val bar = Bar()
                for (notable in notables) {
                    if (!bar.addNotable(notable)) {
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

    private fun forEachTimeSignatureParams(block: (count: Int, noteLength: NoteLength, tpqn: Int) -> Unit) {
        for (count in 1..12) {
            for (noteLength in NoteLength.values()) {
                for (tpqn in listOf(24, 48, 96, 240, 480, 960)) {
                    block(count, noteLength, tpqn)
                }
            }
        }
    }

    private fun forEachTimeSignature(block: (timeSignature: TimeSignature) -> Unit) {
        forEachTimeSignatureParams { count, noteLength, tpqn ->
            block(TimeSignature(count, noteLength, tpqn))
        }
    }

    private fun permutateNotables(block: (notable: List<Notable>) -> Unit) {
        for (notables in Collections2.permutations(createNotables())) {
            block(notables)
        }
    }

    private fun createNotables(): List<Notable> {
        val notables = mutableListOf<Notable>()
        for (noteLengthIdx in 0..NoteLength.values().count() - 1) {
            val noteLength = NoteLength.values()[noteLengthIdx]
            notables.add(Note(noteLength, 0))
        }
        return notables
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
