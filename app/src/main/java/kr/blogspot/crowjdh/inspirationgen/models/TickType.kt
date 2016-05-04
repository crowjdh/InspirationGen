package kr.blogspot.crowjdh.inspirationgen.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * TickType
 *  description
 */
interface TickType {
    fun ticks(timeSignature: TimeSignature): Int
}
