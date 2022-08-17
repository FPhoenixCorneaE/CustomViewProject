package com.example.customviewproject.c.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.minus
import com.example.customviewproject.ext.contains
import com.example.customviewproject.ext.dp
import kotlin.math.*

/**
 *
 * @ClassName: C1View
 * @Author: 史大拿
 * @CreateDate: 8/17/22$ 10:35 AM$
 * TODO
 */
class C1View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    companion object {
        // 大圆半径
        private val BIG_RADIUS = 20.dp

        // 小圆半径
        private val SMALL_RADIUS = BIG_RADIUS

        // 最大范围，超出这个范围大圆不显示
        private val MAX_RADIUS = 150.dp
    }

    // 大圆位置
    private val bigPointF by lazy { PointF(width / 2f, height / 2f) }

    // 小圆位置
    private val smallPointF by lazy { PointF(width / 2f, height / 2f) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                bigPointF.x = event.x
                bigPointF.y = event.y
            }
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.RED


        // 两圆之间的距离
        val d = distance()
        var ratio = d / MAX_RADIUS
        if (ratio > 0.6) {
            ratio = 0.6f
        }
        Log.e("szj两个圆之间的距离", "距离为:$d\t${ratio}")

        // 小圆半径
        val smallRadius = SMALL_RADIUS - SMALL_RADIUS * ratio
        // 小圆
        canvas.drawCircle(smallPointF.x,
            smallPointF.y,
            smallRadius,
            paint)

        // 如果大圆的x < 小圆的x + 最大距离 或者 如果大圆的x > 小圆的x - 最大距离
//        val isDrawBigX =
//            bigPointF.x < smallPointF.x + MAX_RADIUS && bigPointF.x > smallPointF.x - MAX_RADIUS
//
//        val isDrawBigY =
//            bigPointF.y < smallPointF.y + MAX_RADIUS && bigPointF.y > smallPointF.y - MAX_RADIUS
//        if (isDrawBigX && isDrawBigY) {
        if (bigPointF.contains(smallPointF, MAX_RADIUS)) {
            // 大圆
            canvas.drawCircle(bigPointF.x, bigPointF.y, BIG_RADIUS, paint)

            // 绘制贝塞尔
            drawBezier(canvas, smallRadius, BIG_RADIUS)
        }


        // 辅助圆范围
        paint.color = Color.argb(30, 255, 0, 0)
        canvas.drawCircle(smallPointF.x, smallPointF.y, MAX_RADIUS, paint)

    }

    /*
     * 作者:史大拿
     * 创建时间: 8/17/22 3:11 PM
     * TODO 绘制贝塞尔曲线
     * @param smallRadius: 小圆半径
     * @param bigRadius: 大圆半径
     */
    private fun drawBezier(canvas: Canvas, smallRadius: Float, bigRadius: Float) {

        val current = bigPointF - smallPointF

        val BF = current.y.toDouble()
        val FD = current.x.toDouble()
        // BDF = BAD
        val BDF = atan(BF / FD)

        val p1X = smallPointF.x + smallRadius * sin(BDF)
        val p1Y = smallPointF.y - smallRadius * cos(BDF)

        val p2X = bigPointF.x + bigRadius * sin(BDF)
        val p2Y = bigPointF.y - bigRadius * cos(BDF)

        // 控制点
        val controlPointX = current.x / 2 + smallPointF.x
        val controlPointY = current.y / 2 + smallPointF.y

        val p3X = smallPointF.x - smallRadius * sin(BDF)
        val p3Y = smallPointF.y + smallRadius * cos(BDF)

        val p4X = bigPointF.x - bigRadius * sin(BDF)
        val p4Y = bigPointF.y + bigRadius * cos(BDF)

        val path = Path()
        path.moveTo(p1X.toFloat(), p1Y.toFloat())
        path.quadTo(controlPointX, controlPointY, p2X.toFloat(), p2Y.toFloat())

        path.lineTo(p4X.toFloat(), p4Y.toFloat())
        path.quadTo(controlPointX, controlPointY, p3X.toFloat(), p3Y.toFloat())
        path.close()
        canvas.drawPath(path, paint)

    }

    // 小圆与大圆之间的距离
    private fun distance(): Float {
        val current = bigPointF - smallPointF
        return sqrt(current.x.toDouble().pow(2.0) + (current.y.toDouble().pow(2.0))).toFloat()
    }


}

