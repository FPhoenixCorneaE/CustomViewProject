package com.example.customviewproject.a.view.path_effect

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: DashPathEffectView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 1:26 PM$
 * TODO
 */
class DashPathEffectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        strokeWidth = 2.dp
        style = Paint.Style.STROKE

        // 常用来设置虚线
        // @param 1: 画20像素，空50像素，画10像素，空5像素
        // @param 2: 虚线偏移量
        pathEffect = DashPathEffect(floatArrayOf(20f, 50f, 10f, 5f), 0f)
    }

    val path by lazy {
        HeadOriginLineView.path
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        canvas.drawPath(path, paint)
    }
}