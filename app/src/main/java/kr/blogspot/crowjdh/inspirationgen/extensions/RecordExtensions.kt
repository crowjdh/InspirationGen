package kr.blogspot.crowjdh.inspirationgen.extensions

import kr.blogspot.crowjdh.inspirationgen.music.models.Record
import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 17.
 *
 * RecordExtensions
 */

val <T: Record> T.isInDatabase: Boolean
    get() = _id > Record.invalidId

fun <T: Record> T.insert() {
    if (!isInDatabase) {
        records?.forEach { it.insert() }
        database.insert(this, javaClass.kotlin)
    }
}

fun <T: Record> T.update(block: (T.() -> Unit)? = null) {
    val refinedBlock = {
        val prevId = _id
        block?.invoke(this)
        _id = prevId
    }
    if (!isInDatabase) {
        refinedBlock()
        return
    }

    if (records != null) {
        val prevItems: List<Record> = ArrayList(records)
        refinedBlock()

        var tempIdx = Record.invalidId
        records!!.forEach {
            if (!it.isInDatabase) {
                it._id = tempIdx--
            }
        }

        val itemsToInsert = records!!.subtract(prevItems)
        val itemsToDelete = prevItems.subtract(records!!)
        itemsToInsert.forEach {
            it.insert()
        }
        itemsToDelete.forEach {
            it.delete()
        }
        records!!.forEach { it.update() }
    } else {
        refinedBlock()
    }
    database.update(this, javaClass.kotlin)
}

fun <T: Record> T.insertOrUpdate(block: (T.() -> Unit)? = null) {
    if (isInDatabase) {
        update(block)
    } else {
        block?.invoke(this)
        insert()
    }
}

fun <T: Record> T.delete() {
    if (isInDatabase) {
        records?.forEach { it.delete() }
        database.delete(this, javaClass.kotlin)
    }
    _id = Record.invalidId
}
