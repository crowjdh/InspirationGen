package kr.blogspot.crowjdh.inspirationgen.extensions

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 19.
 *
 * CollectionsExtensions
 */

fun <T> List<T>.firstOrDefault(default: T) = firstOrNull() ?: default
