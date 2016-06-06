package kr.blogspot.crowjdh.inspirationgen.extensions

import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 6. 6.
 *
 * ContextExtensions
 */

fun Context.startActivity(clazz: KClass<*>) {
    startActivity(Intent(this, clazz.java))
}
