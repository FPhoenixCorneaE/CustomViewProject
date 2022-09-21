package com.example.customviewproject.e.e3

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.diagonalDistance
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
        val MIN_RADIUS = 20.dp

        // 多边形间隔
        val INTERVAL = 40.dp

        // 多边形条数
        const val POLYGON_COUNT = 3

        // 点数
        const val COUNT = 10

        // 小圆点大小
        val SMALL_RADIUS = 5.dp
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    private val centerLocation by lazy {
        PointF(width / 2f, height / 2f)
    }

    override fun onDraw(canvas: Canvas) {

        // 绘制背景多边形
        drawBackPolygon(canvas)

        val a = getLocation(0, 3f)
        val b = getLocation(1, 2.2f)
        val c = getLocation(2, 1f)

        path.moveTo(a.x, a.y)
        path.lineTo(b.x, b.y)
        path.lineTo(c.x, c.y)

        paint.color = Color.RED
        canvas.drawPath(path, paint)
    }


    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 7:11 PM
     * TODO 获取对应位置
     * @number : 编号
     * @score: 成绩
     * @return PointF: <x X坐标> <y Y坐标>
     */
    private fun getLocation(number: Int, score: Float): PointF = let {
        // 角度
        val angle = 360 / COUNT * number
        // 半径
        val radius = (score - 1) * INTERVAL + MIN_RADIUS
        val x =
            (radius * cos(Math.toRadians(angle.toDouble())) + centerLocation.x).toFloat()
        val y =
            (radius * sin(Math.toRadians(angle.toDouble())) + centerLocation.y).toFloat()

        return PointF(x, y)
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
        (0 until POLYGON_COUNT).forEachIndexed { index, element ->

            (0 until COUNT).forEach { count ->
                // 当前是第几条 * 间隔 + 最小半径 = 当前半径
                val radius = element * INTERVAL + MIN_RADIUS
                val x =
                    (radius * cos(Math.toRadians(count * eachAngle.toDouble())) + centerLocation.x).toFloat()
                val y =
                    (radius * sin(Math.toRadians(count * eachAngle.toDouble())) + centerLocation.y).toFloat()

//                canvas.drawCircle(x, y, SMALL_RADIUS, paint)
                if (count == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }


                if (index == POLYGON_COUNT - 1) {

                    val text = "知行合一"

                    val rect = Rect()

                    paint.getTextBounds(text, 0, text.length, rect)
                    val textWidth = rect.width()
                    val textHeight = rect.height()

                    val tempRadius = radius + textHeight
                    val textX =
                        (tempRadius * cos(Math.toRadians(count * eachAngle.toDouble())) + centerLocation.x).toFloat() - textWidth / 2f
                    val textY =
                        (tempRadius * sin(Math.toRadians(count * eachAngle.toDouble())) + centerLocation.y).toFloat()

                    paint.textSize = 16.dp
                    paint.style = Paint.Style.FILL
                    canvas.drawText(text, textX, textY, paint)

//                    canvas.drawCircle(textX, textY, 5.dp, paint)
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