package com.example.customviewproject.b.view.b2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: B2PointView
 * @Author: 史大拿
 * @CreateDate: 8/12/22$ 7:28 PM$
 * TODO 多点触控 默认画板(不支持多点触控)
 */
class B23TouchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        strokeWidth = 6.dp
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val path = Path()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 将点移动过去
                path.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {

            }
        }

        invalidate() // 刷新
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    // 清空画板
    fun clear() {
        path.reset()
        invalidate()
    }
}