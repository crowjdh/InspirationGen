package kr.blogspot.crowjdh.inspirationgen.extensions

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 17.
 *
 * HashCodeExtensions
 */

fun hashCodeWith(vararg items: Any?): Int {
    var hashCode = 1
    for (item in items) {
        hashCode = item.hashCode(hashCode)
    }
    return hashCode
}

private fun Any?.hashCode(base: Int): Int {
    return 31 * base + (this?.hashCode() ?: 0)
}
