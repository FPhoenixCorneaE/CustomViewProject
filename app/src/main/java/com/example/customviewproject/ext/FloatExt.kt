package com.example.customviewproject.ext

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.pow
import kotlin.math.sqrt

/**
 *
 * @ClassName: FloatExt
 * @Author: 史大拿
 * @CreateDate: 8/5/22$ 2:37 PM$
 * TODO
 */

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this,
        Resources.getSystem().displayMetrics
    )

/*
 * 作者:史大拿
 * 创建时间: 9/21/22 7:30 PM
 * TODO 对角线距离
 * (a平方 + b平方) 开根号
 */
infix fun Float.diagonalDistance(b: Float): Float = let {
    val a = this
    return sqrt(a.toDouble().pow(2.0) + b.toDouble().pow(2.0)).toFloat()
}