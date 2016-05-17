package kr.blogspot.crowjdh.inspirationgen.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import kr.blogspot.crowjdh.inspirationgen.database.InsGenDbContract.Bar
import kr.blogspot.crowjdh.inspirationgen.database.InsGenDbContract.Sheet

/**
 * Created by Dongheyon Jeong in Randombox_Android from Yooii Studios Co., LTD. on 15. 8. 11.

 * InsGenDbHelper
 */
class InsGenDbHelper(context: Context) : SQLiteOpenHelper(context, InsGenDbHelper.databaseName, null, InsGenDbHelper.databaseVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        createAllTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    private fun createAllTables(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_SHEET_ENTRY)
        db.execSQL(SQL_CREATE_BAR_ENTRY)
    }

    companion object {
        private val databaseVersion = 1
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
                        Sheet.barIds    + type_text +
                        " )"
        private val SQL_CREATE_BAR_ENTRY =
                "CREATE TABLE " + Bar.tableName + "(" +
                        Bar._id                     + type_autoIncrement + comma_sep +
                        Bar.barIndex                + type_int + comma_sep +
                        Bar.encodedNotables         + type_text + comma_sep +
                        Bar.timeSignatureCount      + type_int + comma_sep +
                        Bar.timeSignatureNoteLength + type_int +
                        " )"
    }
}
