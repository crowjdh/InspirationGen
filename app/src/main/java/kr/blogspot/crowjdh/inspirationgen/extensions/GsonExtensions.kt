package kr.blogspot.crowjdh.inspirationgen.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 18.
 *
 * GsonExtensions
 */

inline fun <reified T> genericType() = object: TypeToken<T>() {}.type

inline fun <reified T> Gson.fromJson(jsonString: String?): T {
    return fromJson<T>(jsonString, genericType<T>())
}

fun Any?.toGsonString(): String = Gson().toJson(this)

inline fun <reified T> String?.fromGsonString(): T {
    return Gson().fromJson<T>(this)
}
