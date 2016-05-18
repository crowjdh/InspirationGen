package kr.blogspot.crowjdh.inspirationgen.database

/**
 * Created by Dongheyon Jeong in Randombox_Android from Yooii Studios Co., LTD. on 15. 8. 11.
 *
 * InsGenDbContract
 */
abstract class InsGenDbContract {

    interface Table {
        val tableName: String
    }

    abstract class Sheet {
        companion object Entry: Table {
            val _id = "_id"
            override val tableName = "sheets"
            val name = "name"
            val barIds = "bar_ids"
        }
    }

    abstract class Bar {
        companion object Entry: Table {
            val _id = "_id"
            override val tableName = "bars"
            val timeSignatureCount = "time_signature_count"
            val timeSignatureNoteLength = "time_signature_note_length"
            val encodedNotables = "encoded_notables"
        }
    }
}
