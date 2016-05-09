package kr.blogspot.crowjdh.inspirationgen.helpers

import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength
import kr.blogspot.crowjdh.inspirationgen.music.models.TimeSignature

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 7.
 *
 * TestTimeSignatures
 *  TimeSignature generator for test.
 */

class TestTimeSignatures {

    companion object Generator {

        fun forEachTimeSignatureParams(block: (count: Int, noteLength: NoteLength) -> Unit) {
            for (count in 1..12) {
                for (noteLength in NoteLength.values()) {
                    block(count, noteLength)
                }
            }
        }

        fun forEachTimeSignature(block: (timeSignature: TimeSignature) -> Unit) {
            forEachTimeSignatureParams { count, noteLength ->
                block(TimeSignature(count, noteLength))
            }
        }
    }
}
