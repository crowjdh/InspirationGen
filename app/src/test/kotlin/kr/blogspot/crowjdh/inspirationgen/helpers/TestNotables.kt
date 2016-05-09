package kr.blogspot.crowjdh.inspirationgen.helpers

import com.google.common.collect.Collections2
import kr.blogspot.crowjdh.inspirationgen.music.models.Notable
import kr.blogspot.crowjdh.inspirationgen.music.models.Note
import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 7.
 *
 * TestNotables
 *  Notable generator for test.
 */

class TestNotables {

    companion object Generator {

        fun permutateNotables(block: (notable: List<Notable>) -> Unit) {
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
    }
}