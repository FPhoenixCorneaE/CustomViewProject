package com.example.customviewproject.a.view.gradient

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: LinearGradientView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 4:54 PM$
 * TODO
 */
class OriginHeadGradientView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

        val paint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2.dp
            color = Color.YELLOW
        }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(150.dp.toInt(), widthMeasureSpec)
        val height = resolveSize(202.dp.toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(100.dp, 100.dp, 100.dp, paint)
    }
}