package com.example.customviewproject.e.e3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @ClassName: E3PolygonChartView
 * @Author: 史大拿
 * @CreateDate: 9/21/22$ 3:49 PM$
 * TODO 多边形图表
 */
class E3PolygonChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 最小多边形
        val MIN_RADIUS = 40.dp

        // 多边形间隔
        val INTERVAL = 40.dp

        // 多边形条数
        const val POLYGON_COUNT = 4

        // 点数
        const val COUNT = 5

        // 小圆点大小
        val SMALL_RADIUS = 5.dp
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    override fun onDraw(canvas: Canvas) {

        // 绘制背景多边形
        drawBackPolygon(canvas)
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 4:11 PM
     * TODO 绘制背景多边形
     */
    private fun drawBackPolygon(canvas: Canvas) {
//        paint.color = Color.RED
//        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

        val eachAngle = 360 / COUNT
        (0 until POLYGON_COUNT).forEach { element ->

            (0 until COUNT).forEach { count ->
                // 当前是第几条 * 间隔 + 最小半径 = 当前半径
                val radius = element * INTERVAL + MIN_RADIUS
                val x =
                    (radius * cos(Math.toRadians(count * eachAngle.toDouble())) + width / 2f).toFloat()
                val y =
                    (radius * sin(Math.toRadians(count * eachAngle.toDouble())) + height / 2f).toFloat()

//                canvas.drawCircle(x, y, SMALL_RADIUS, paint)
                if (count == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            path.close()

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2.dp
            canvas.drawPath(path, paint)
            path.reset()
        }


    }
}