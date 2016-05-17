package kr.blogspot.crowjdh.inspirationgen

import android.support.test.runner.AndroidJUnit4
import android.test.ApplicationTestCase
import android.util.Log
import kr.blogspot.crowjdh.inspirationgen.extensions.*
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 15.
 *
 * AndroidJUnitSampleTest
 */
@RunWith(AndroidJUnit4::class)
class SqlBriteTest():
        ApplicationTestCase<InspirationGenApplication>(InspirationGenApplication::class.java) {

    companion object {
        val TAG = "AndroidJUnitSampleTest"
    }

    @Before
    fun setup() {
        super.setUp()
        val database = InspirationGenDatabase.get()

        val sheetTableName = getMapperOf<Sheet>(Sheet::class)?.tableName
        if (sheetTableName != null) {
            database.delete(sheetTableName, null)
        }
    }

    @After
    fun teardown() {
    }

    @Test
    fun test() {
        InspirationGenDatabase.get().observeTable<Sheet>(Sheet::class) {
            var message = "it.count: ${it.count()}"
            if (it.count() > 0) {
                message += ", it.first().name: ${it.first().name}"
            }
            Log.i(TAG, message)
        }
        var firstSheet: Sheet? = null
        var secondSheet: Sheet? = null
        for (idx in 1..5) {
            val sheet = Sheet()
            when (idx) {
                1 -> firstSheet = sheet
                2 -> secondSheet = sheet
            }

            sheet.insert()
        }
        firstSheet!!.update { name = "changedName" }
        secondSheet!!.delete()
    }
}
