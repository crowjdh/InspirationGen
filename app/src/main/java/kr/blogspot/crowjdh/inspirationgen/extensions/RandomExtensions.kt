package kr.blogspot.crowjdh.inspirationgen.extensions

import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 9.
 *
 * RandomExtensions
 */

fun <T> Random.pickFromMap(keyToWeightMap: Map<T, Int?>): T {
    fun Int?.orOne() = this ?: 1
    val denominator = keyToWeightMap.map { it.value.orOne() }.reduce { prev, cur -> prev + cur }
    val randomInt = nextInt(denominator)

    var accumulated = 0
    keyToWeightMap.forEach {
        if (randomInt >= accumulated && randomInt < accumulated + it.value.orOne()) {
            return it.key
        }
        accumulated += it.value.orOne()
    }

    // Fallback
    throw Exception("This Exception SHOULD NOT be thrown!!")
}
