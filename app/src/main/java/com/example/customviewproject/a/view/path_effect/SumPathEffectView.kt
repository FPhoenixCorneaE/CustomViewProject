package com.example.customviewproject.a.view.path_effect

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.dp


/**
 *
 * @ClassName: CornerPathEffectView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 1:26 PM$
 * TODO
 */
class SumPathEffectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        strokeWidth = 2.dp
        style = Paint.Style.STROKE

        // 用来画虚线
        val dashEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)

        // 用来画抖动的线
        val discreteEffect = DiscretePathEffect(20f, 20f)

        // 两个PathEffect一起使用
        pathEffect = SumPathEffect(dashEffect, discreteEffect)
    }

    val path by lazy {
        HeadOriginLineView.path
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        canvas.drawPath(path, paint)
    }
}