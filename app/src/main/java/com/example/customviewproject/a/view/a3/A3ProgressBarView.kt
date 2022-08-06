package com.example.customviewproject.a.view.a3

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: ProgressBarView
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 4:12 PM$
 * TODO 普通进度条View
 */
class A3ProgressBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()

    // 位置
    private lateinit var rect: RectF

    companion object {
        private val RADIUS = 50.dp

        // 当前进度
        @FloatRange(from = 0.0, to = 100.0)
        private val CURRENT_PROGRESS = 90.0

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect = RectF(width / 2 - RADIUS, height / 2 - RADIUS,
            width / 2 + RADIUS, height / 2 + RADIUS)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO  绘制背景圆环
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20.dp
        canvas.drawOval(
            rect,
            paint
        )
        // TODO 绘制红色进度
        paint.color = Color.RED
        paint.strokeCap = Paint.Cap.ROUND // 圆角
        // userCenter 是否连接居中线
        canvas.drawArc(rect, -90f, (360 / 100f * CURRENT_PROGRESS).toFloat(), false, paint)


        val text = "$CURRENT_PROGRESS%"
        paint.textSize = RADIUS / 3 // 文字大小
        paint.textAlign = Paint.Align.CENTER // 文字横向居中
        paint.style = Paint.Style.FILL// 文字样式
        val fontMetrics = paint.fontMetrics // 绘制文字是baseline线 用来求居中线(垂直)

        // 绘制文字
        canvas.drawText(text,
            0,
            text.length,
            width / 2f,
            height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2,
            paint)
    }
}