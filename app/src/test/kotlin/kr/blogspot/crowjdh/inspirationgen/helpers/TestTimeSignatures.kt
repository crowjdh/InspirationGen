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

        fun forEachTimeSignatureParams(block: (count: Int, noteLength: NoteLength, tpqn: Int) -> Unit) {
            for (count in 1..12) {
                for (noteLength in NoteLength.values()) {
                    for (tpqn in listOf(24, 48, 96, 240, 480, 960)) {
                        block(count, noteLength, tpqn)
                    }
                }
            }
        }

        fun forEachTimeSignature(block: (timeSignature: TimeSignature) -> Unit) {
            forEachTimeSignatureParams { count, noteLength, tpqn ->
                block(TimeSignature(count, noteLength, tpqn))
            }
        }
    }
}
