package com.example.customviewproject.a.view.gradient

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: LinearGradientView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 4:54 PM$
 * TODO 扫描渐变
 */
class SweepGradientView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint().apply {
        style = Paint.Style.FILL
        strokeWidth = 2.dp
        color = Color.YELLOW
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
         * @param cx,cy:扫描中心点
         * @param color1:扫描开始的颜色
         * @param color2:扫描结束的颜色
         */
        val shader = SweepGradient(
            width / 4f, width / 4f, Color.RED, Color.YELLOW
        )
        paint.shader = shader
        canvas.drawCircle(width / 4f, width / 4f, width / 4f, paint)
    }
}