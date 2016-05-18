package kr.blogspot.crowjdh.inspirationgen

import android.app.Application
import android.content.Context

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 17.
 *
 * InspirationGenApplication
 */

class InspirationGenApplication: Application() {
    companion object {
        lateinit private var _context: Context

        val context: Context
            get() = _context
    }

    override fun onCreate() {
        _context = this
        super.onCreate()
    }
}
