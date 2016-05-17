package kr.blogspot.crowjdh.inspirationgen.extensions

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import kr.blogspot.crowjdh.inspirationgen.InspirationGenApplication
import kr.blogspot.crowjdh.inspirationgen.database.InsGenDbContract
import kr.blogspot.crowjdh.inspirationgen.database.InsGenDbHelper
import kr.blogspot.crowjdh.inspirationgen.music.models.Record
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import rx.Subscription
import rx.schedulers.Schedulers
import kotlin.reflect.KClass

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 16.
 *
 * BriteDatabaseExtensions
 */

private val TAG = "BriteDatabaseExtensions"

fun <T: Record> BriteDatabase.observeTable(clazz: KClass<*>, onNext: (it: List<T>) -> Unit)
        : Subscription? {
    val mapperMeta = getMapperOf<T>(clazz) ?: return null

    val observable = createQuery(mapperMeta.tableName, "SELECT * FROM ${mapperMeta.tableName}")
    // TODO: Consider adding distinct filter
    return observable.mapToList { cursor -> mapperMeta.cursorToTypeMapper(cursor) }
            .subscribeOn(Schedulers.io())
            .subscribe({ onNext(it) }) {
                Log.e(TAG, "Exception thrown while subscribing table ${mapperMeta.tableName}", it)
            }
}

fun <T: Record> BriteDatabase.insert(item: T, clazz: KClass<*>) {
    val mapperMeta = getMapperOf<T>(clazz) ?: return

    val values = mapperMeta.typeToContentValuesMapper(item)

    item._id = insert(mapperMeta.tableName, values)
}

fun <T: Record> BriteDatabase.update(item: T, clazz: KClass<*>) {
    val mapperMeta = getMapperOf<T>(clazz) ?: return

    val values = mapperMeta.typeToContentValuesMapper(item)
    update(mapperMeta.tableName, values, "_id=?", item._id.toString())
}

fun <T: Record> BriteDatabase.delete(item: T, clazz: KClass<*>) {
    val mapperMeta = getMapperOf<T>(clazz) ?: return

    delete(mapperMeta.tableName, "_id=?", item._id.toString())
}

fun <T: Record> getMapperOf(clazz: KClass<*>): MapperMeta<T>? {
    @Suppress("UNCHECKED_CAST")
    return modelToMapperMap[clazz] as? MapperMeta<T>
}

object InspirationGenDatabase {

    @Volatile private var instance: BriteDatabase? = null

    fun get(): BriteDatabase {
        if (InspirationGenDatabase.instance == null) {
            synchronized (InspirationGenDatabase::class.java) {
                if (InspirationGenDatabase.instance == null) {
                    InspirationGenDatabase.instance = SqlBrite.create().wrapDatabaseHelper(
                            InsGenDbHelper(InspirationGenApplication.context), Schedulers.io())
                }
            }
        }
        return instance!!
    }
}

class MapperMeta<T>(val tableName: String,
                    val typeToContentValuesMapper: (T) -> ContentValues,
                    val cursorToTypeMapper: (Cursor) -> T)

private val sheetToContentValuesMapper: (sheet: Sheet) -> ContentValues = {
    val values = ContentValues()
    values.put(InsGenDbContract.Sheet.name, it.name)
    // TODO: Change to real values
    values.put(InsGenDbContract.Sheet.barIds, "0,1,2")
    values
}

private val cursorToSheetMapper: (cursor: Cursor) -> Sheet = {
    val sheet = Sheet()
    sheet.name = it.getString(it.getColumnIndex(InsGenDbContract.Sheet.name))
    sheet
}

private val modelToMapperMap = hashMapOf<KClass<*>, MapperMeta<*>>(Pair(Sheet::class,
        MapperMeta(InsGenDbContract.Sheet.tableName,
                sheetToContentValuesMapper,
                cursorToSheetMapper)))
