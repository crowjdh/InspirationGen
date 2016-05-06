package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Notable
 *  A musical component which has length and can be presented on a music sheet.
 */

interface Notable: TickType {

    val length: NoteLength

    override fun ticks(timeSignature: TimeSignature) = length.ticks(timeSignature.tpqn)
}
