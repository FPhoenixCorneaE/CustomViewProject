package com.example.customviewproject.util.bezier

import android.animation.TypeEvaluator
import android.graphics.PointF
import android.util.Log

/*
 * 作者:史大拿
 * 创建时间: 9/6/22 2:30 PM
 * TODO 三阶贝塞尔
 */
class ThirdBezierTypeEvaluator(private val p1: PointF, private val p2: PointF) :
    TypeEvaluator<PointF> {

        // p0开始点; p1控制点1; p2控制点2; p3结束点; t当前进度(0..1);
        override fun evaluate(t: Float, p0: PointF, p3: PointF): PointF {
            // 三阶贝塞尔公式地址: https://baike.baidu.com/item/贝塞尔曲线/1091769

//            pointF.x = p0.x * ((1 - t).pow(3))
//            +3 * p1.x * t * ((1 - t).pow(2))
//            +3 * p2.x * t.pow(2) * (1 - t)
//            +p3.x * t.pow(3)
//
//            pointF.y = p0.y * ((1 - t).pow(3))
//            +3 * p1.y * t * ((1 - t).pow(2))
//            +3 * p2.y * t.pow(2) * (1 - t)
//            +p3.y * t.pow(3)
            return PointF(
                p0.x * (1 - t) * (1 - t) * (1 - t)
                        + 3 * p1.x * t * (1 - t) * (1 - t)
                        + 3 * p2.x * t * t * (1 - t)
                        + p3.x * t * t * t,
                p0.y * (1 - t) * (1 - t) * (1 - t)
                        + 3 * p1.y * t * (1 - t) * (1 - t)
                        + 3 * p2.y * t * t * (1 - t)
                        + p3.y * t * t * t
            )
        }
    }