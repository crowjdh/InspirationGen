package kr.blogspot.crowjdh.inspirationgen

import kr.blogspot.crowjdh.inspirationgen.extensions.fromGsonString
import kr.blogspot.crowjdh.inspirationgen.extensions.toGsonString
import kr.blogspot.crowjdh.inspirationgen.music.models.*
import org.junit.Test
import java.util.concurrent.atomic.AtomicLong
import kotlin.test.assertEquals

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 18.
 *
 * GsonTest
 */

class GsonTest {

    @Test
    fun encodeAndDecodeBarGeneratorOptions_equals() {
        val options = Bar.Generator.Options.create {
            timeSignature = TimeSignature(3, NoteLength.HALF)
            scale = Scale(80..81)
            barCount = 2
            noteOverRestBias = .8f
            noteLengthRange = Bar.Generator.NoteLengthRange.create(
                    Pair(NoteLength.QUARTER, 20), Pair(NoteLength.EIGHTH, 80))
            atomicBaseSeed = AtomicLong(123)
        }

        val optionsJsonString = options.toGsonString()
        val optionsFromJson = optionsJsonString.fromGsonString<Bar.Generator.Options>()

        assertEquals(options, optionsFromJson)
    }

    @Test
    fun encodeAndDecodeSheetOptions_equals() {
        val options = Sheet.Options.create { bpm = 140 }

        val optionsJsonString = options.toGsonString()
        val optionsFromJson = optionsJsonString.fromGsonString<Sheet.Options>()

        assertEquals(options, optionsFromJson)
    }
}
