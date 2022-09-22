package com.example.customviewproject.ext

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.pow
import kotlin.math.sqrt

/**
 *
 * @ClassName: IntExt
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 4:14 PM$
 * TODO
 */
val Int.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(),
        Resources.getSystem().displayMetrics)

infix fun Int.diagonalDistance(b: Int): Float = let {
    val a = this
    return sqrt(a.toDouble().pow(2.0) + b.toDouble().pow(2.0)).toFloat()
}