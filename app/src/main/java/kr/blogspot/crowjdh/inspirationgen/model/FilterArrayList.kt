package kr.blogspot.crowjdh.inspirationgen.model

import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 17.
 *
 * FilterArrayList
 *  ArrayList with filter which filters elements when added.
 */

class FilterArrayList<T>(val filter: (T) -> Boolean): ArrayList<T>() {

    override fun add(element: T): Boolean {
        if (filter(element)) {
            return super.add(element)
        } else {
            return false
        }
    }

    override fun add(index: Int, element: T) {
        if (filter(element)) {
            super.add(index, element)
        }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        throw UnsupportedOperationException("addAll method is not supported yet")
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        throw UnsupportedOperationException("addAll method is not supported yet")
    }
}
