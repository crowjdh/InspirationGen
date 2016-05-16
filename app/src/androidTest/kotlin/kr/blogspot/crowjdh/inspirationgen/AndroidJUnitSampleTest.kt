package kr.blogspot.crowjdh.inspirationgen

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.properties.Delegates
import kotlin.test.assertNotNull

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 15.
 *
 * AndroidJUnitSampleTest
 */
@RunWith(AndroidJUnit4::class)
class AndroidJUnitSampleTest {

    private var context: Context by Delegates.notNull()

    @Before
    fun setup() {
        context = InstrumentationRegistry.getTargetContext()
        assertNotNull(context)
    }

    @After
    fun teardown() {
    }

    @Test
    fun test() {
    }
}