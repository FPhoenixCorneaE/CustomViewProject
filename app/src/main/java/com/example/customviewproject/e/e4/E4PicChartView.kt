package com.example.customviewproject.e.e4

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.*
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @ClassName: E4PicChartView
 * @Author: 史大拿
 * @CreateDate: 9/27/22$ 4:41 PM$
 * TODO
 */
class E4PicChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        val RADIUS = 200.dp

        // 选中移动距离
        val DISTANCE = 20.dp
    }

    // 点击图表
    private var clickPosition = 3

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val data = listOf(1f, 2f, 3f, 2f)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize((RADIUS + DISTANCE * 2).toInt(), widthMeasureSpec)
        val height = resolveSize(width, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 每一格的大小
        val each = 360 / totalNumber

        when (event.action) {
            MotionEvent.ACTION_UP -> {

                val angle = (PointF(event.x, event.y)).angle(PointF(width / 2f, height / 2f))

                var startAngle = 0f
                data.forEachIndexed { index, value ->
                    // 每一格的占比
                    val ration = each * value

                    if (angle in startAngle..(startAngle + ration)) {
                        Log.e("szjDOWNIndex1", "$index")
                        clickPosition = index
                        return true
                    }

                    startAngle += ration
                }

            }
            MotionEvent.ACTION_MOVE -> {

            }
        }
        invalidate()
        return true
    }

    // 总数
    private val totalNumber: Float
        get() {
            return data.fold(0f) { a, b -> a + b }
        }

    override fun onDraw(canvas: Canvas) {

        // 每一格的大小
        val each = 360 / totalNumber

        // 居中显示
        val left = width / 2f - RADIUS / 2f
        val top = height / 2f - RADIUS / 2f
        val right = left + RADIUS
        val bottom = top + RADIUS

        var startAngle = 0f
        data.forEachIndexed { position, value ->


            // 每一格的占比
            val ration = each * value
            paint.color = colorRandom
            if (position == clickPosition % data.size) {
                canvas.save()
                val dx =
                    DISTANCE * cos(Math.toRadians(startAngle.toDouble() + ration / 2f)).toFloat()
                val dy =
                    DISTANCE * sin(Math.toRadians(startAngle.toDouble() + ration / 2f)).toFloat()
                canvas.translate(dx, dy)
            }

            canvas.drawArc(left, top, right, bottom, startAngle, ration, true, paint)
            startAngle += ration


            if (position == clickPosition % data.size) {
                canvas.restore()
            }
        }


        paint.color = Color.BLACK
        canvas.drawCircle(width / 2f, height / 2f, 10.dp, paint)

    }
}