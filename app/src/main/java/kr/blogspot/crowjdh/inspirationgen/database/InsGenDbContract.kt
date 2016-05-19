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
            val bpm = "bpm"
            val barIds = "bar_ids"
            val pinned = "pinned"
        }
    }

    abstract class Bar {
        companion object Entry: Table {
            val _id = "_id"
            override val tableName = "bars"
            val timeSignatureCount = "time_signature_count"
            val timeSignatureNoteLength = "time_signature_note_length"
            val encodedNotables = "encoded_notables"
            val program = "program"
        }
    }

    abstract class SheetOptions {
        companion object Entry: Table {
            val _id = "_id"
            override val tableName = "sheet_options"
            val bpm = "bpm"
        }
    }

    abstract class BarOptions {
        companion object Entry: Table {
            val _id = "_id"
            override val tableName = "bars_options"
            val timeSignature = "time_signature"
            val scale = "scale"
            val noteLengthRange = "note_length_range"
            val barCount = "bar_count"
            val noteOverRestBias = "note_over_rest_bias"
            val program = "program"
            val atomicBaseSeed = "atomic_base_seed"
        }
    }
}
