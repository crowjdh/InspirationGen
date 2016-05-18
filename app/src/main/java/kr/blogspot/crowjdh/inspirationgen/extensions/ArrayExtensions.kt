package kr.blogspot.crowjdh.inspirationgen.extensions

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 18.
 *
 * ArrayExtensions
 */

fun Array<Int>.toPrefixSum(): List<Int> {
    val prefixSum = mutableListOf<Int>()
    foldIndexed(0) { idx, prev, cur ->
        val sum = prev + cur
        prefixSum.add(sum)
        sum
    }
    return prefixSum
}
