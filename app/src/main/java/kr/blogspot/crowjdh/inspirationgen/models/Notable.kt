package kr.blogspot.crowjdh.inspirationgen.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Notable
 *  description
 */

interface Notable: TickType {

    val length: NoteLength

    override fun ticks(timeSignature: TimeSignature) = length.ticks(timeSignature.tpqn)
}
