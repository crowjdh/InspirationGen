package kr.blogspot.crowjdh.inspirationgen.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kr.blogspot.crowjdh.inspirationgen.database.InsGenDbContract.*


/**
 * Created by Dongheyon Jeong in Randombox_Android from Yooii Studios Co., LTD. on 15. 8. 11.

 * InsGenDbHelper
 */
class InsGenDbHelper(context: Context) : SQLiteOpenHelper(context, InsGenDbHelper.databaseName,
        null, InsGenDbHelper.databaseVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        createAllTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropTable(db, Sheet.tableName)
        dropTable(db, Bar.tableName)
        dropTable(db, SheetOptions.tableName)
        dropTable(db, BarOptions.tableName)
        createAllTables(db)
    }

    private fun createAllTables(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_SHEET_ENTRY)
        db.execSQL(SQL_CREATE_BAR_ENTRY)
        db.execSQL(SQL_CREATE_SHEET_OPTIONS_ENTRY)
        db.execSQL(SQL_CREATE_BAR_OPTIONS_ENTRY)
    }

    private fun dropTable(db: SQLiteDatabase, tableName: String) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName)
    }

    companion object {
        private val databaseVersion = 4
        private val invalidDatabaseVersion = -1
        private val databaseName = "InsGenDatabase"

        // Macros
        private val type_autoIncrement = " INTEGER PRIMARY KEY AUTOINCREMENT"
        private val type_text = " TEXT"
        private val type_int = " INTEGER"
        private val type_double = " DOUBLE"
        private val option_default = " DEFAULT "
        private val comma_sep = ","

        private val SQL_CREATE_SHEET_ENTRY =
                "CREATE TABLE " + Sheet.tableName + "(" +
                        Sheet._id       + type_autoIncrement + comma_sep +
                        Sheet.name      + type_text + comma_sep +
                        Sheet.bpm       + type_int + comma_sep +
                        Sheet.barIds    + type_text +
                        " )"
        private val SQL_CREATE_BAR_ENTRY =
                "CREATE TABLE " + Bar.tableName + "(" +
                        Bar._id                     + type_autoIncrement + comma_sep +
                        Bar.encodedNotables         + type_text + comma_sep +
                        Bar.timeSignatureCount      + type_int + comma_sep +
                        Bar.timeSignatureNoteLength + type_int +
                        " )"
        private val SQL_CREATE_SHEET_OPTIONS_ENTRY =
                "CREATE TABLE " + SheetOptions.tableName + "(" +
                        SheetOptions._id    + type_autoIncrement + comma_sep +
                        SheetOptions.bpm    + type_int +
                        " )"
        private val SQL_CREATE_BAR_OPTIONS_ENTRY =
                "CREATE TABLE " + BarOptions.tableName + "(" +
                        BarOptions._id              + type_autoIncrement + comma_sep +
                        BarOptions.timeSignature    + type_text + comma_sep +
                        BarOptions.scale            + type_text + comma_sep +
                        BarOptions.noteLengthRange  + type_text + comma_sep +
                        BarOptions.barCount         + type_text + comma_sep +
                        BarOptions.noteOverRestBias + type_text + comma_sep +
                        BarOptions.atomicBaseSeed   + type_text +
                        " )"
    }
}
