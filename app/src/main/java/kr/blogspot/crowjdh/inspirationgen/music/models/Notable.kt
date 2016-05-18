package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Notable
 *  A musical component which has length and can be presented on a music sheet.
 */

interface Notable: TickType {

    val length: NoteLength

    override val ticks: Long
        get() = length.ticks().toLong()

    val description: String
        get() = "${length.length}"

    companion object Parser {

        fun encode(notable: Notable): String {
            return when (notable) {
                is Note -> "${notable.description}|${notable.pitch}"
                is Rest -> "${notable.description}"
                else -> throw UnsupportedOperationException("Encoding $notable is not yet supported")
            }
        }

        fun encode(notables: List<Notable>): String
                = notables.fold("") { prev, cur -> prev + Notable.encode(cur) + "," }

        fun decodeNotable(description: String): Notable {
            if (description.contains('|')) {
                val parts = description.split('|').filter { it.length > 0 }
                return Note(NoteLength.fromLength(parts[0].toFloat())!!, parts[1].toInt())
            } else {
                return Rest(NoteLength.fromLength(description.toFloat())!!)
            }
        }

        fun decodeNotables(notablesDescription: String): List<Notable> {
            return notablesDescription.split(',').filter { it.length > 0 }.map { decodeNotable(it) }
        }
    }
}
