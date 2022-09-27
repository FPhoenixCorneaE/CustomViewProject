package com.example.customviewproject.e.e3_blog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.e.e3.E3PolygonChartView
import com.example.customviewproject.e.e3.E3PolygonChartView.Companion.POLYGON_BACK_COLOR
import com.example.customviewproject.ext.angle
import com.example.customviewproject.ext.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @ClassName: E3PolygonChartBlogView
 * @Author: 史大拿
 * @CreateDate: 9/27/22$ 1:30 PM$
 * TODO
 */
class E3PolygonChartBlogView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 半径
        val SMALL_RADIUS = 30.dp

        // 几边形(有几个点)
        // TODO 注意和 data 同步, 建议查看 E3PolygonChartView.kt
        const val COUNT = 5

        // 有几条边
        const val NUMBER = 5

        // 每一条边的间隔
        val INTERVAL = 20.dp
    }

    // TODO 注意和 COUNT 同步, 建议查看 E3PolygonChartView.kt
    var data = listOf(3f, 2f, 3f, 1f, 1f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 中点
    private val centerLocation by lazy {
        PointF(width / 2f, height / 2f)
    }

    private val path = Path()

    private var offsetAngle = 0f
    private var downAngle = 0f
    private var originAngle = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downAngle = centerLocation.angle(PointF(event.x, event.y))
                originAngle = offsetAngle
            }
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)

                offsetAngle =
                    centerLocation.angle(PointF(event.x, event.y)) - downAngle + originAngle

                Log.e("szjOffset","$offsetAngle")
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        invalidate()

        return true
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
//        val cx = centerLocation.x
//        val cy = centerLocation.y
//        canvas.drawCircle(cx, cy, SMALL_RADIUS, paint)


        // 每一个的间隔
        val eachAngle = 360 / COUNT
        // 循环有几条边
        (0 until NUMBER).forEachIndexed { index, element ->

            // 循环每一条边有几个点
            (0 until COUNT).forEach { count ->
                // 半径 = 当前是第几条边 * 间距 + 最中间的距离
                val radius = element * INTERVAL + SMALL_RADIUS
                val angle = count * eachAngle.toDouble() + offsetAngle

                val x =
                    (radius * cos(Math.toRadians(angle)) + centerLocation.x).toFloat()
                val y =
                    (radius * sin(Math.toRadians(angle)) + centerLocation.y).toFloat()
                if (count == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }

                // 当前是最后一层
                if (index == NUMBER - 1) {
                    paint.strokeWidth = 2.dp
                    paint.style = Paint.Style.STROKE
                    paint.color = POLYGON_BACK_COLOR

                    // 最内层x,y 坐标
//                    val stopX =
//                        (SMALL_RADIUS * cos(Math.toRadians(angle)) + centerLocation.x).toFloat()
//                    val stopY =
//                        (SMALL_RADIUS * sin(Math.toRadians(angle)) + centerLocation.y).toFloat()
//                    canvas.drawLine(x, y, stopX, stopY, paint)
                    canvas.drawLine(x, y, centerLocation.x, centerLocation.y, paint)
                }


                // 设置文字
                if (index == NUMBER - 1) {
                    val text = "文字${count}"

                    val rect = Rect()

                    // 计算文字宽高 计算完成之后会把值赋值给rect
                    paint.getTextBounds(text, 0, text.length, rect)
                    val textWidth = rect.width()
                    val textHeight = rect.height()

                    val tempRadius = radius + textHeight
                    val textX =
                        (tempRadius * cos(Math.toRadians(angle)) + centerLocation.x).toFloat() - textWidth / 2f
                    val textY =
                        (tempRadius * sin(Math.toRadians(angle)) + centerLocation.y).toFloat()

                    paint.textSize = 16.dp
                    paint.style = Paint.Style.FILL
                    paint.color = E3PolygonChartView.TEXT_COLOR
                    canvas.drawText(text, textX, textY, paint)

//                    canvas.drawCircle(textX, textY, 5.dp, paint)
                }
            }
            path.close() // 闭合
            paint.strokeWidth = 2.dp
            paint.style = Paint.Style.STROKE
            paint.color = POLYGON_BACK_COLOR

            canvas.drawPath(path, paint)
            path.reset()
        }

        // 绘制数据
        drawArea(canvas)
    }

    private fun drawArea(canvas: Canvas) {
        data.forEachIndexed { index, value ->
            val location = getLocation(index, value)

            if (index == 0) {
                path.moveTo(location.x, location.y)
            } else {
                path.lineTo(location.x, location.y)
            }
        }
        path.close()


        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        canvas.drawPath(path, paint) // 绘制边

        paint.style = Paint.Style.FILL
        paint.alpha = (255 * 0.1).toInt()
        canvas.drawPath(path, paint) // 绘制内边
        path.reset()
    }


    /*
     * 作者:史大拿
     * 创建时间: 9/27/22 2:54 PM
     * @number 第几个点
     * @count 第几条边
     */
    private fun getLocation(number: Int, count: Float): PointF = let {
        // 角度
        val angle = 360 / COUNT * number + offsetAngle
        // 半径
        val radius = (count - 1) * INTERVAL + SMALL_RADIUS
        val x =
            (radius * cos(Math.toRadians(angle.toDouble())) + centerLocation.x).toFloat()
        val y =
            (radius * sin(Math.toRadians(angle.toDouble())) + centerLocation.y).toFloat()

        return PointF(x, y)
    }
}