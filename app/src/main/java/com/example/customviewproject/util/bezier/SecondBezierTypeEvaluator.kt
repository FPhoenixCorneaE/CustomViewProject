package com.example.customviewproject.util.bezier

import android.animation.TypeEvaluator
import android.graphics.PointF
import kotlin.math.pow

/// 二阶贝塞尔曲线
class SecondBezierTypeEvaluator(private val p1: PointF) :
    TypeEvaluator<PointF> {
    // p0开始点; p1控制点; p2结束点;
    override fun evaluate(t: Float, p0: PointF, p2: PointF): PointF {
        // 二阶贝塞尔公式地址: https://baike.baidu.com/item/贝塞尔曲线/1091769

        return PointF(
            (1 - t).pow(2) * p0.x + 2 * t * (1 - t) * p1.x + t.pow(2) * p2.x,
            (1 - t).pow(2) * p0.y + 2 * t * (1 - t) * p1.y + t.pow(2) * p2.y
        )
    }
}