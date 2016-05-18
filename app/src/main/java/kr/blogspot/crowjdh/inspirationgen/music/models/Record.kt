package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 16.
 *
 * Record
 *  A record for models to persist.
 */
interface Record {

    var _id: Long

    val records: List<Record>?

    companion object {
        val invalidId: Long = 0
    }
}
