package kr.blogspot.crowjdh.inspirationgen.extensions

import kr.blogspot.crowjdh.inspirationgen.music.models.Record

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 17.
 *
 * RecordExtensions
 */

val <T: Record> T.isInDatabase: Boolean
    get() = _id != null

fun <T: Record> T.insert() {
    if (!isInDatabase) {
        InspirationGenDatabase.get().insert(this, javaClass.kotlin)
    }
}

fun <T: Record> T.update(block: (T.() -> Unit)? = null) {
    block?.invoke(this)
    if (isInDatabase) {
        InspirationGenDatabase.get().update(this, javaClass.kotlin)
    } else {
        _id = null
    }
}

fun <T: Record> T.delete() {
    if (isInDatabase) {
        InspirationGenDatabase.get().delete(this, javaClass.kotlin)
    }
    _id = null
}
