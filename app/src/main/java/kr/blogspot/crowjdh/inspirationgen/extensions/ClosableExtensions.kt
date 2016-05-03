package kr.blogspot.crowjdh.inspirationgen.extensions

import java.io.Closeable

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 3.
 *
 * ClosableExtensions
 *  description
 */

inline fun <T: Closeable, R> T.use(block: T.() -> R): R {
    use { closable ->
        return closable.block()
    }
}
