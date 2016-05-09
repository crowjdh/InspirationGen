package kr.blogspot.crowjdh.inspirationgen.extensions

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 8.
 *
 * MutableMapExtensions
 */

inline fun <K> MutableMap<K, Int>.increaseOrPut(key: K, defaultValue: () -> Int)
        = set(key, getOrPut(key, defaultValue) + 1)
