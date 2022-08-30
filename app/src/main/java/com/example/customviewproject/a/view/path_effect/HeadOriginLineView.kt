package com.example.customviewproject.a.view.path_effect

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: HeadOriginLineView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 1:26 PM$
 * TODO
 */
class HeadOriginLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {
        strokeWidth = 4.dp
        style = Paint.Style.STROKE
    }

    companion object {


        val path by lazy {
            Path().apply {
                moveTo(20.dp, 20.dp)
                lineTo(50.dp, 50.dp)
                lineTo(100.dp, 200.dp)
                lineTo(150.dp, 50.dp)
                lineTo(250.dp, 20.dp)
                lineTo(200.dp, 200.dp)
                lineTo(300.dp, 200.dp)
                lineTo(350.dp, 100.dp)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(350.dp.toInt(), widthMeasureSpec)
        val height = resolveSize(204.dp.toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }
}