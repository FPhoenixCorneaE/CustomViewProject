package com.example.customviewproject.util.bezier

import android.animation.TypeEvaluator
import android.graphics.PointF
import android.util.Log
import kotlin.math.pow

/// 二阶贝塞尔曲线
class SecondListBezierTypeEvaluator(private val p1: List<PointF>) :
    TypeEvaluator<List<PointF>> {
    // p0开始点; p1控制点; p2结束点
    override fun evaluate(t: Float, p0: List<PointF>, p2: List<PointF>): List<PointF> {
        // 二阶贝塞尔公式地址: https://baike.baidu.com/item/贝塞尔曲线/1091769

        if (!(p0.size == p1.size && p0.size == p2.size)) {
            throw RuntimeException("长度不匹配")
        }

        val points = arrayListOf<PointF>()
        repeat(p0.size) {
            points.add(
                PointF(
                    (1 - t).pow(2) * p0[it].x + 2 * t * (1 - t) * p1[it].x + t.pow(2) * p2[it].x,
                    (1 - t).pow(2) * p0[it].y + 2 * t * (1 - t) * p1[it].y + t.pow(2) * p2[it].y
                )
            )
        }
        return points
    }
}