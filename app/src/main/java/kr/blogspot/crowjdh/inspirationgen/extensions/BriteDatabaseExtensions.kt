package kr.blogspot.crowjdh.inspirationgen.extensions

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import kr.blogspot.crowjdh.inspirationgen.InspirationGenApplication
import kr.blogspot.crowjdh.inspirationgen.database.InsGenDbContract
import kr.blogspot.crowjdh.inspirationgen.database.InsGenDbHelper
import kr.blogspot.crowjdh.inspirationgen.music.models.*
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.reflect.KClass

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 16.
 *
 * BriteDatabaseExtensions
 */

private val TAG = "BriteDatabaseExtensions"

private fun selectSql(tableName: String) = "SELECT * FROM $tableName"

val database: BriteDatabase
    get() = InspirationGenDatabase.get()

fun <T: Record> BriteDatabase.observeTable(clazz: KClass<*>, onNext: (it: List<T>) -> Unit)
        : Subscription {
    val mapperMeta = getMapperOf<T>(clazz)

    return tableObservable<T>(clazz).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ onNext(it) }) {
        Log.e(TAG, "Exception thrown while subscribing table ${mapperMeta.tableName}", it)
    }
}

fun <T: Record> BriteDatabase.tableObservable(clazz: KClass<*>)
        : Observable<List<T>> {
    val mapperMeta = getMapperOf<T>(clazz)

    val observable = createQuery(mapperMeta.tableName, selectSql(mapperMeta.tableName))
    return observable.mapToList { cursor -> mapperMeta.cursorToTypeMapper(cursor) }
            .distinctUntilChanged { it.hashCode() }
}

fun <T: Record> BriteDatabase.insert(item: T, clazz: KClass<*>) {
    val mapperMeta = getMapperOf<T>(clazz)

    val values = mapperMeta.typeToContentValuesMapper(item)
    item._id = insert(mapperMeta.tableName, values)
}

fun <T: Record> BriteDatabase.all(clazz: KClass<*>): List<T> {
    val mapperMeta = getMapperOf<T>(clazz)
    return queryCursor(mapperMeta.tableName).all { mapperMeta.cursorToTypeMapper(this) }

}

fun <T: Record> BriteDatabase.select(clazz: KClass<*>, _id: Long): T? {
    val mapperMeta = getMapperOf<T>(clazz)
    return queryCursor(mapperMeta.tableName, _id).first { mapperMeta.cursorToTypeMapper(this) }
}

private fun BriteDatabase.queryCursor(tableName: String, _id: Long? = null): Cursor {
    var sql = "${selectSql(tableName)}"
    if (_id != null) {
        sql = "$sql WHERE _id=$_id"
    }
    return query(sql)
}

fun <T: Record> BriteDatabase.update(item: T, clazz: KClass<*>) {
    val mapperMeta = getMapperOf<T>(clazz)

    val values = mapperMeta.typeToContentValuesMapper(item)
    update(mapperMeta.tableName, values, "_id=?", item._id.toString())
}

fun <T: Record> BriteDatabase.delete(item: T, clazz: KClass<*>) {
    val mapperMeta = getMapperOf<T>(clazz)
    delete(mapperMeta.tableName, "_id=?", item._id.toString())
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

// MapperMeta

class MapperMeta<T>(val tableName: String,
                    val typeToContentValuesMapper: (T) -> ContentValues,
                    val cursorToTypeMapper: (Cursor) -> T)

fun <T: Record> getMapperOf(clazz: KClass<*>): MapperMeta<T> {
    @Suppress("UNCHECKED_CAST")
    return modelToMapperMap[clazz] as MapperMeta<T>
}

private val sheetToContentValuesMapper: (sheet: Sheet) -> ContentValues = {
    val values = ContentValues()
    values.put(InsGenDbContract.Sheet.name, it.name)

    val barIds = it.bars.fold("") { prev, cur -> "$prev,${cur._id}" }
    values.put(InsGenDbContract.Sheet.bpm, it.bpm)
    values.put(InsGenDbContract.Sheet.barIds, barIds)

    values
}

private val cursorToSheetMapper: (cursor: Cursor) -> Sheet = {
    val sheet = Sheet()
    sheet._id = it.getLong(InsGenDbContract.Sheet._id)
    sheet.bpm = it.getInt(InsGenDbContract.Sheet.bpm)
    sheet.name = it.getString(InsGenDbContract.Sheet.name)

    val barIds = it.getString(InsGenDbContract.Sheet.barIds).split(',')
            .filter { it.length > 0 }.map { it.toLong() }
    for (barId in barIds) {
        val bar = database.select<Bar>(Bar::class, barId)
        if (bar != null) {
            sheet.bars.add(bar)
        }
    }

    sheet
}

private val barToContentValuesMapper: (bar: Bar) -> ContentValues = {
    val values = ContentValues()

    values.put(InsGenDbContract.Bar.timeSignatureCount, it.timeSignature.count)
    values.put(InsGenDbContract.Bar.timeSignatureNoteLength, it.timeSignature.noteLength.length)
    values.put(InsGenDbContract.Bar.encodedNotables, Notable.encode(it.notables))
    values
}

private val cursorToBarMapper: (cursor: Cursor) -> Bar = {
    val bar = Bar()

    bar._id = it.getLong(InsGenDbContract.Bar._id)
    val count = it.getInt(InsGenDbContract.Bar.timeSignatureCount)
    val length = it.getInt(InsGenDbContract.Bar.timeSignatureNoteLength)
    bar.timeSignature = TimeSignature(count, NoteLength.fromLength(length.toFloat())!!)

    val encodedNotables = it.getString(InsGenDbContract.Bar.encodedNotables)
    Notable.decodeNotables(encodedNotables).forEach { bar.notables.add(it) }

    bar
}

private val sheetOptionsToContentValuesMapper: (options: Sheet.Options) -> ContentValues = {
    val values = ContentValues()

    values.put(InsGenDbContract.SheetOptions.bpm, it.bpm)
    values
}

private val cursorToSheetOptionsMapper: (cursor: Cursor) -> Sheet.Options = {
    Sheet.Options.create {
        _id = it.getLong(InsGenDbContract.SheetOptions._id)
        bpm = it.getInt(InsGenDbContract.SheetOptions.bpm)
    }
}

private val barOptionsToContentValuesMapper: (options: Bar.Generator.Options) -> ContentValues = {
    val values = ContentValues()

    values.put(InsGenDbContract.BarOptions.timeSignature, it.timeSignature.toGsonString())
    values.put(InsGenDbContract.BarOptions.scale, it.scale.toGsonString())
    values.put(InsGenDbContract.BarOptions.noteLengthRange, it.noteLengthRange.toGsonString())
    values.put(InsGenDbContract.BarOptions.barCount, it.barCount.toGsonString())
    values.put(InsGenDbContract.BarOptions.noteOverRestBias, it.noteOverRestBias.toGsonString())
    values.put(InsGenDbContract.BarOptions.atomicBaseSeed, it.atomicBaseSeed?.toGsonString())
    values
}

private val cursorToBarOptionsMapper: (cursor: Cursor) -> Bar.Generator.Options = {
    Bar.Generator.Options.create {
        _id = it.getLong(InsGenDbContract.BarOptions._id)
        timeSignature = it.getString(InsGenDbContract.BarOptions.timeSignature).fromGsonString()
        scale = it.getString(InsGenDbContract.BarOptions.scale).fromGsonString()
        noteLengthRange = it.getString(InsGenDbContract.BarOptions.noteLengthRange).fromGsonString()
        barCount = it.getString(InsGenDbContract.BarOptions.barCount).fromGsonString()
        noteOverRestBias = it.getString(InsGenDbContract.BarOptions.noteOverRestBias).fromGsonString()
        atomicBaseSeed = it.getString(InsGenDbContract.BarOptions.atomicBaseSeed).fromGsonString()
    }
}

private val modelToMapperMap = hashMapOf<KClass<*>, MapperMeta<*>>(
        Pair(Sheet::class,
                MapperMeta(InsGenDbContract.Sheet.tableName,
                        sheetToContentValuesMapper,
                        cursorToSheetMapper)),
        Pair(Bar::class,
                MapperMeta(InsGenDbContract.Bar.tableName,
                        barToContentValuesMapper,
                        cursorToBarMapper)),
        Pair(Sheet.Options::class,
                MapperMeta(InsGenDbContract.SheetOptions.tableName,
                        sheetOptionsToContentValuesMapper,
                        cursorToSheetOptionsMapper)),
        Pair(Bar.Generator.Options::class,
                MapperMeta(InsGenDbContract.BarOptions.tableName,
                        barOptionsToContentValuesMapper,
                        cursorToBarOptionsMapper)))
