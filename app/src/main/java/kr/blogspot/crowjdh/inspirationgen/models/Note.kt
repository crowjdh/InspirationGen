package kr.blogspot.crowjdh.inspirationgen.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Note
 */

data class Note(override val length: NoteLength, val pitch: Int): Notable
