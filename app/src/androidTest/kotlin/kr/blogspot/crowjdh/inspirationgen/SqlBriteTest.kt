package kr.blogspot.crowjdh.inspirationgen

import android.support.test.runner.AndroidJUnit4
import android.test.ApplicationTestCase
import android.util.Log
import kr.blogspot.crowjdh.inspirationgen.extensions.*
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.NoteLength
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import rx.Observable
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

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

        fun createSheet(postfix: String): Sheet {
            val sheet = Sheet(name = "sheet_$postfix")
            sheet.bars.addAll(createBars())
            return sheet
        }

        fun createBars(): List<Bar> {
            return Bar.generate {
                barCount = 2
                noteOverRestBias = .8f
                noteLengthRange = Bar.Generator.NoteLengthRange.create(
                        Pair(NoteLength.QUARTER, 20), Pair(NoteLength.EIGHTH, 80))
                atomicBaseSeed = AtomicLong(100)
            }
        }
    }

    @Before
    fun setup() {
        super.setUp()
        val database = InspirationGenDatabase.get()

        database.delete(getMapperOf<Sheet>(Sheet::class).tableName, null)
        database.delete(getMapperOf<Bar>(Bar::class).tableName, null)
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

    @Test
    fun testOneShotObservable() {
        val stop = AtomicBoolean(false)
        val stopTwo = AtomicBoolean(false)
        Observable.just(1, 2, 3, 4).takeUntil { stop.get() }.subscribe {
            Log.i(TAG, "This message will be displayed multiple times")
        }
        Observable.just(1, 2, 3, 4).takeUntil { stop.get() }.doOnNext { stop.set(true) }.subscribe {
            Log.i(TAG, "1, 2, 3, 4. This message will be displayed only once.")
        }
        InspirationGenDatabase.get().tableObservable<Sheet>(Sheet::class)
                .takeUntil { stopTwo.get() }.doOnNext { stopTwo.set(true) }.subscribe {
            Log.i(TAG, "tableObservable. This message will be displayed only once.")
        }
    }

    @Test
    fun createSheet_equalsToQueriedSheet() {
        val sheet = createSheet("one")
        sheet.insert()

        assertEqualsToSavedSheet(sheet)
    }

    @Test
    fun modifyAndDeleteSheet_equalsToQueriedSheet() {
        val sheet = createSheet("one")
        sheet.insert()

        var remainedBars: MutableList<Bar>? = null
        sheet.update {
            _id = -99
            name = "changedName"
            bars.removeAt(0)
            val addedBars = createBars()
            bars.addAll(addedBars)

            remainedBars = mutableListOf(bars[0])
            remainedBars!!.addAll(addedBars)
        }

        assertEquals(remainedBars!!, sheet.bars)
        assertEqualsToSavedSheet(sheet)

        sheet.delete()

        val sheetCount = InspirationGenDatabase.get().all<Sheet>(Sheet::class).count()
        val barCount = InspirationGenDatabase.get().all<Bar>(Bar::class).count()
        assertEquals(0, sheetCount)
        assertEquals(0, barCount)
    }

    private fun assertEqualsToSavedSheet(sheet: Sheet) {
        val selectedSheet = InspirationGenDatabase.get().select<Sheet>(Sheet::class, sheet._id)
        assertEquals(sheet, selectedSheet)
    }
}
