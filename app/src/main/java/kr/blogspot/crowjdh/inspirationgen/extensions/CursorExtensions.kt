package kr.blogspot.crowjdh.inspirationgen.extensions

import android.database.Cursor

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 18.
 *
 * CursorExtensions
 */

fun Cursor.getString(columnName: String) = getString(getColumnIndex(columnName))
fun Cursor.getLong(columnName: String) = getLong(getColumnIndex(columnName))
fun Cursor.getInt(columnName: String) = getInt(getColumnIndex(columnName))

inline fun <T> Cursor.first(block: Cursor.(Cursor) -> T): T? {
    var selected: T? = null
    if (count > 0) {
        moveToFirst()
        selected = block(this)
        close()
    }
    return selected
}

inline fun <T> Cursor.all(block: Cursor.(Cursor) -> T): List<T> {
    val selected: MutableList<T> = mutableListOf()
    if (count > 0) {
        moveToFirst()
        do {
            selected.add(block(this))
        } while (moveToNext())
        close()
    }
    return selected
}
