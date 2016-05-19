package kr.blogspot.crowjdh.inspirationgen.extensions

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 19.
 *
 * PrimitiveExtensions
 */

fun Boolean.toInt(): Int = if (this) 1 else 0

fun Int.toBoolean(): Boolean = if (this > 0) true else false
