package com.example.customviewproject.ext

import android.graphics.PointF
import androidx.core.graphics.minus
import kotlin.math.pow
import kotlin.math.sqrt

/**
 *
 * @ClassName: PointFExt
 * @Author: 史大拿
 * @CreateDate: 8/17/22$ 11:12 AM$
 * TODO
 */
/*
 * 作者:史大拿
 * 创建时间: 8/17/22 11:10 AM
 * TODO 判断当前PointF是否在 bPointF内
 *  bPadding: 外边距
 * return: 在内返回true
 */
fun PointF.contains(b: PointF, bPadding: Float = 0f): Boolean {
    val isX = this.x <= b.x + bPadding && this.x >= b.x - bPadding

    val isY = this.y <= b.y + bPadding && this.y >= b.y - bPadding
    return isX && isY
}

/*
 * 作者:史大拿
 * 创建时间: 9/13/22 1:26 PM
 * TODO 两个PointF之间的距离
 * @param a: 开始点
 * @param b: 结束点
 * @return: 通过勾股定理算两点之间的距离
 */
fun PointF.distance(b: PointF): Float = let {
    val a = this

    // 这里 * 1.0 是为了转Double
    val dx = b.x - a.x * 1.0
    val dy = b.y - a.y * 1.0

    return@let sqrt(dx.pow(2) + dy.pow(2)).toFloat()
}
