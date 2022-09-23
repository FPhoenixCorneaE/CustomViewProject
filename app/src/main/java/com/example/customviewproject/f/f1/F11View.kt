package com.example.customviewproject.f.f1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
class F11View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    companion object {
        val RADIUS = 10.dp
    }

    var mCx = RADIUS

    // 步长
    var step = 5
    override fun onDraw(canvas: Canvas) {
        val cx = mCx
        val cy = height / 2f
        canvas.drawCircle(cx, cy, RADIUS, paint)

        paint.strokeWidth = 2.dp
        canvas.drawLine(
            0f,
            height / 2f + RADIUS,
            width.toFloat(),
            height / 2f + RADIUS,
            paint
        )

        mCx += step

        if (mCx >= width) {
            step = -5
            paint.color = colorRandom
        } else if (mCx <= 0) {
            step = 5
            paint.color = colorRandom
        }


        invalidate()
    }
}