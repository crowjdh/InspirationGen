package kr.blogspot.crowjdh.inspirationgen.helpers

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 9.
 *
 * PrintHelper
 */

fun printSection(title: String, preBreakLineCount: Int = 2) {
    val preBreakLines = "\n".repeat(preBreakLineCount)
    val delimiter = "/".repeat(((80 - title.length) / 2) - 1)

    println("$preBreakLines$delimiter $title $delimiter")
}
