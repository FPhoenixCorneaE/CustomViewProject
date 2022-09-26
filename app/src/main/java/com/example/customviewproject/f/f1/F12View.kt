package com.example.customviewproject.f.f1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.colorRandom
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: F11View
 * @Author: 史大拿
 * @CreateDate: 9/23/22$ 7:58 PM$
 * TODO
 */
class F12View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    companion object {
        val RADIUS = 10.dp

        // 滚动速度 每帧/px
        const val SPEED = 5f
    }

    private var cx = 0f
    private var cy = 0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (cx == 0f && cy == 0f) {
            cx = w / 2f
            cy = h / 2f
        }
    }

    // 步长
    private var step = PointF(SPEED, SPEED)

    override fun onDraw(canvas: Canvas) {
        val left = width * 1 / 4f
        val top = height * 1 / 4f
        val right = width * 3 / 4f
        val bottom = height * 3 / 4f

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.dp
        canvas.drawRect(left, top, right, bottom, paint)


        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, RADIUS, paint)
        cx += step.x
        cy += step.y

        when {
            cx >= right -> {
                step.x = -SPEED
                paint.color = colorRandom

            }
            cx <= left -> {
                step.x = SPEED
                paint.color = colorRandom

            }
            cy <= top -> {
                step.y = SPEED
                paint.color = colorRandom
            }
            cy >= bottom -> {
                step.y = -SPEED
                paint.color = colorRandom
            }
        }

        invalidate()
    }


}