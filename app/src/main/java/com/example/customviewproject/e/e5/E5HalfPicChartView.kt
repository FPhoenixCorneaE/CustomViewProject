package com.example.customviewproject.e.e5

import android.animation.ObjectAnimator
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
import com.example.customviewproject.e.e4.E4PicChartView
import com.example.customviewproject.ext.angle
import com.example.customviewproject.ext.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @ClassName: E5HalfPicChartView
 * @Author: 史大拿
 * @CreateDate: 9/30/22$ 9:55 AM$
 * TODO
 */
open class E5HalfPicChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val WIDTH = 200.dp

        private val HEIGHT = WIDTH / 2

        // 选中移动距离
        val DISTANCE = 20.dp

        // 开场动画时间
        const val ANIMATOR = 1000L
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    open var data = listOf(
        Triple(Color.RED, 1f, "红色"),
        Triple(Color.WHITE, 1f, "白色"),
        Triple(Color.YELLOW, 1f, "黄色"),
        Triple(Color.GREEN, 1f, "绿色"),
    )

    // 每一个角度
    private val eachAngle: Float
        get() {
            // 总数
            val totalNumber = data.map { it.second }.fold(0f) { a, b -> a + b }
            return 180f / totalNumber
        }


    // 选中位置
    open var selectPosition = -1

    private var currentFraction = -1f

    private val animator by lazy {
        val animator = ObjectAnimator.ofFloat(0f, 1f)
        animator.duration = ANIMATOR
        animator.addUpdateListener {
            currentFraction = it.animatedValue as Float
            invalidate()
        }
        animator
    }

    init {
        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = resolveSize((WIDTH + DISTANCE * 2).toInt(), widthMeasureSpec)
        val height = resolveSize((HEIGHT + DISTANCE).toInt(), widthMeasureSpec)
        setMeasuredDimension(width, height)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {
                val angle = (PointF(x, y)).angle(PointF(width / 2f, height.toFloat()))
                Log.e("szjAngle", "$angle")
                var startAngle = 180f
                data.forEachIndexed { index, element ->

                    // 开始位置
                    val start = startAngle

                    // 结束位置
                    val end = start + element.second * eachAngle

                    if (angle in start..end) {
                        Log.e("szjIndex", "$index")
                        selectPosition = if (selectPosition == index && selectPosition != -1) {
                            -1
                        } else {
                            // 否则重新赋值
                            index
                        }
                        invalidate()
                        return true
                    }
                    startAngle = end
                }
            }
        }
        return true
    }


    override fun onDraw(canvas: Canvas) {

        val left = width / 2f - WIDTH / 2f
        val top = height / 2f - HEIGHT / 2f + DISTANCE / 2f
        val right = left + WIDTH
        val bottom = top + HEIGHT * 2

//        canvas.drawRect(left, top, right, bottom, paint)

        var startAngle = 0f
        data.forEachIndexed { index, element ->

            var sweepAngle = element.second * eachAngle

            // 画布是否保存
            val isSave = index == (selectPosition % data.size)

            if (isSave) {
                canvas.save()

                val angle = (startAngle.toDouble() + 180f) + sweepAngle / 2f

                Log.e("szj默认", "start:${startAngle}\tration:${sweepAngle}")
                val dx =
                    E4PicChartView.DISTANCE * cos(Math.toRadians(angle)).toFloat()
                val dy =
                    E4PicChartView.DISTANCE * sin(Math.toRadians(angle)).toFloat()
                canvas.translate(dx, dy)
            }

            paint.color = element.first

            // 设置动画
//            startAngle *= currentFraction
            sweepAngle *= currentFraction

            canvas.drawArc(left, top, right, bottom, startAngle + 180f, sweepAngle, true, paint)

            startAngle += sweepAngle


            if (isSave) {
                canvas.restore()
            }
        }
    }
}