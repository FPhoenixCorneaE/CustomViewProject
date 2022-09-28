package com.example.customviewproject.e.e4

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.angle
import com.example.customviewproject.ext.angle2
import com.example.customviewproject.ext.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @ClassName: E4PicChartView
 * @Author: 史大拿
 * @CreateDate: 9/27/22$ 4:41 PM$
 * TODO
 */
open class E4PicChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        val RADIUS = 200.dp

        // 选中移动距离
        val DISTANCE = 20.dp
    }

    // 点击图表
    open var clickPosition = -1

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val data = listOf(
        Triple(Color.RED, 1f, "红色"),
        Triple(Color.WHITE, 2f, "白色"),
        Triple(Color.YELLOW, 3f, "黄色"),
        Triple(Color.GREEN, 4f, "绿色"),
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize((RADIUS + DISTANCE * 2).toInt(), widthMeasureSpec)
        val height = resolveSize(width, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private var offsetAngle = 0f
    private var downAngle = 0f
    private var downPointF = PointF()
    private var originAngle = 0f


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 每一格的大小
        val each = 360 / totalNumber

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downAngle = (PointF(event.x, event.y)).angle(PointF(width / 2f, height / 2f))
                originAngle = offsetAngle

                downPointF.x = event.x
                downPointF.y = event.y

            }
            MotionEvent.ACTION_MOVE -> {

                // 滑动过程中始终保持未按下状态
//                clickPosition = -1
                parent.requestDisallowInterceptTouchEvent(true)

                offsetAngle = (PointF(event.x, event.y)).angle(
                    PointF(
                        width / 2f,
                        height / 2f
                    )
                ) - downAngle + originAngle
                Log.e("szjOffsetAngle", "$offsetAngle")
            }
            MotionEvent.ACTION_UP -> {

                // 当前角度
                var angle =
                    (PointF(event.x, event.y)).angle2(PointF(width / 2f, height / 2f))

                angle = getNormalizedAngle(angle)

                // 当前滑动距离
                val offset = getNormalizedAngle(offsetAngle)
                val a = getNormalizedAngle(angle - offset)
                Log.e(
                    "szjAngle",
                    "a:$a\tangle:${angle}\toffset:${offset}"
                )

                var startAngle = 0f

                data.forEachIndexed { index, value ->
                    // 每一格的占比
                    val ration = each * value.second

                    startAngle += ration

                    if (startAngle > a) {
                        clickPosition = index
                        invalidate()
                        return true
                    }


//                    Log.e(
//                        "szjDOWNIndex2",
//                        "index:${index}\tangle:$angle\ttempStartAngle:${tempStartAngle}\ttempend:$tempEndAngle"
//                    )
//                    if (angle in tempStartAngle..tempEndAngle) {
//                        Log.e("szjDOWNIndex1", "$index")
//                        clickPosition = if (clickPosition == index && clickPosition != -1) {
//                            -1
//                        } else {
//                            index
//                        }
//                        invalidate()
//                        return true
//                    }

                }
            }

        }
        invalidate()
        return true
    }

    open fun getNormalizedAngle(angle: Float): Float {
        var angle = angle
        while (angle < 0f) angle += 360f
        return angle % 360f
    }

    // 总数
    private val totalNumber: Float
        get() {
            return data.map { it.second }.fold(0f) { a, b -> a + b }
        }

    private val path: Path by lazy {
        Path().also {
            it.addCircle(width / 2f, height / 2f, RADIUS / 6f, Path.Direction.CCW)
        }
    }

    override fun onDraw(canvas: Canvas) {

        canvas.rotate(offsetAngle, width / 2f, height / 2f)

        // 需要android版本 >= api26 (8.0)
        canvas.clipOutPath(path)

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
            val ration = each * value.second
            if (position == clickPosition % data.size) {
                canvas.save()

                val dx =
                    DISTANCE * cos(Math.toRadians(startAngle.toDouble() + ration / 2f)).toFloat()
                val dy =
                    DISTANCE * sin(Math.toRadians(startAngle.toDouble() + ration / 2f)).toFloat()
                canvas.translate(dx, dy)
            }
            paint.color = value.first

            // 绘制扇形
            canvas.drawArc(left, top, right, bottom, startAngle, ration, true, paint)

            // 绘制文字
            drawText(canvas, startAngle, startAngle + ration, position)

            startAngle += ration


            if (position == clickPosition % data.size) {
                canvas.restore()
            }
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/28/22 2:29 PM
     * TODO 绘制文字
     * @startAngle: 开始角度
     * @endAngle: 结束角度
     */
    private fun drawText(canvas: Canvas, startAngle: Float, endAngle: Float, position: Int) {
        val ration = (endAngle - startAngle) / 2f + startAngle
        val radius = RADIUS / 3f
//        Log.e("szjDrawText", "start:$startAngle\tend:$endAngle\tration:$ration")

        val dx =
            radius * cos(Math.toRadians(ration * 1.0)).toFloat() + width / 2f
        val dy =
            radius * sin(Math.toRadians(ration * 1.0)).toFloat() + height / 2f


        paint.textSize = 16.dp
//        paint.color = colorRandom
        paint.color = Color.BLACK

//        canvas.drawCircle(dx, dy, 2.dp, paint)

        val text = "${data[position].third}-$position"
        val textWidth = paint.measureText(text) // 文字宽度
        val textHeight = paint.descent() + paint.ascent() // 文字高度

        val textX = dx - (textWidth / 2f)
        val textY = dy - (textHeight / 2f)

        canvas.drawText(text, 0, text.length, textX, textY, paint)
    }
}