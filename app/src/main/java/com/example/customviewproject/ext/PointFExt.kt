package com.example.customviewproject.ext

import android.graphics.PointF
import android.util.Log
import kotlin.math.*

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

/*
 * 作者:史大拿
 * 创建时间: 9/26/22 10:55 AM
 * TODO 计算角度
 * @param startP: 开始点
 * @param endP: 结束点
 */
fun PointF.angle(endP: PointF): Float {
    val startP = this

    // 原始位置
    val angle = if (startP.x >= endP.x && startP.y >= endP.y) {
        Log.e("szjLocation", "end在start右下角")
        0
    } else if (startP.x >= endP.x && startP.y <= endP.y) {
        Log.e("szjLocation", "end在start右上角")
        270
    } else if (startP.x <= endP.x && startP.y <= endP.y) {
        Log.e("szjLocation", "end在start左上角")
        180
    } else if (startP.x <= endP.x && startP.y >= endP.y) {
        Log.e("szjLocation", "end在start左下角")
        90
    } else {
        0
    }
    // 计算距离
    val dx = startP.x - endP.x
    val dy = startP.y - endP.y
    // 弧度
    val radian = abs(atan(dy / dx))

    // 弧度转角度
    var a = Math.toDegrees(radian.toDouble()).toFloat()

    if (startP.x <= endP.x && startP.y >= endP.y) {
        // 左下角
        a = 90 - a
    } else if (startP.x >= endP.x && startP.y <= endP.y) {
        // 右上角
        a = 90 - a
    }
    return a + angle
}

fun PointF.angle2(endP: PointF): Float {
    val startP = this
    val cx = endP.x
    val cy = endP.y
//        Log.i("szjCx", "cx:" + c.getX().toString() + "\t" + "y:" + c.y)
    val tx = startP.x - cx
    val ty = startP.y - cy
    val length = sqrt((tx * tx + ty * ty).toDouble())

    val r = acos(ty / length)

    var angle = Math.toDegrees(r).toFloat()

    if (startP.x > cx) angle = 360f - angle

    // add 90° because chart starts EAST
    angle += 90f

    // neutralize overflow
    if (angle > 360f) angle -= 360f
    return angle
}
