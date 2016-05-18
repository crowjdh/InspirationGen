package kr.blogspot.crowjdh.inspirationgen.extensions

import android.app.Activity
import android.content.Intent
import kotlin.reflect.KClass

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 18.
 *
 * ActivityExtensions
 */

fun Activity.startActivity(clazz: KClass<*>) {
    startActivity(Intent(this, clazz.java))
}
