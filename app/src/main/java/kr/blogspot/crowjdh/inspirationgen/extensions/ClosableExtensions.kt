package kr.blogspot.crowjdh.inspirationgen.extensions

import java.io.Closeable

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 3.
 *
 * ClosableExtensions
 *  Usage:
 *      Built-in "use" function:
 *          resources.openRawResource(resId).use { inputStream ->
 *              inputStream.doSomething1()
 *              inputStream.doSomething2()
 *          }
 *
 *      New "use" function:
 *          resources.openRawResource(resId).use {
 *              this.doSomething1()
 *              doSomething2()
 *          }
 */

inline fun <T: Closeable, R> T.use(block: T.() -> R): R {
    use { closable ->
        return closable.block()
    }
}
