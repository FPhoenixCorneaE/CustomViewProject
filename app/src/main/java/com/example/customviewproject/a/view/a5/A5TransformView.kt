package com.example.customviewproject.a.view.a5

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import androidx.core.graphics.withScale
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: A5TransformView
 * @Author: 史大拿
 * @CreateDate: 8/8/22$ 6:59 PM$
 * TODO 几何变换
 */
class A5TransformView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {
        strokeWidth = 4.dp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 画布 x,y 方向平移
        canvas.translate(100f, 100f)

        // 以中心点旋转30度
        canvas.rotate(30f, width / 2f, height / 2f)

        // 缩放
        canvas.scale(0.5f, 0.5f)

        canvas.drawLine(0f, height.toFloat() / 2, width.toFloat(), height.toFloat() / 2, paint)

        paint.color = Color.RED
        canvas.drawLine(width.toFloat() / 2, 0f, width.toFloat() / 2, height.toFloat(), paint)
    }
}