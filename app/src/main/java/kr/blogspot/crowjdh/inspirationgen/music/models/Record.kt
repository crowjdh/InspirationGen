package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 16.
 *
 * Record
 *  description
 */
interface Record {

    var _id: Long

    val records: List<Record>?

    companion object {
        val invalidId: Long = 0
    }
}
