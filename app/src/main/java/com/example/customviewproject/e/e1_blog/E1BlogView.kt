package com.example.customviewproject.e.e1_blog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.e.e1.view.E1ChartView
import com.example.customviewproject.e.e1.view.E1ChartView.Companion.BACK_COLOR
import com.example.customviewproject.e.e1.view.E1LocationBean
import com.example.customviewproject.ext.contains
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: E1BlogView
 * @Author: 史大拿
 * @CreateDate: 9/19/22$ 3:40 PM$
 * TODO
 */
class E1BlogView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 水平个数
    private val horizontalCount = 5

    // 垂直个数
    private val verticalCount = 5

    private val data = arrayListOf<E1LocationBean>()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (data.size == 0) {
            data.clear()
            // 每一格的宽
            val eachWidth = w / verticalCount
            // 每一格的高
            val eachHeight = h / horizontalCount

            (0 until 5).forEachIndexed { index, value ->
                // 保存每一格的宽高
                // tips:这里 *1f 是为了 Int -> Float
                data.add(
                    E1LocationBean(
                        index * eachWidth * 1f,
                        index * eachHeight * 1f,
                        value
                    )
                )
            }
        }
    }

    private var offsetX = 0f
    private var downX = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
            }

            MotionEvent.ACTION_MOVE -> {
                // 当前偏移位置 = 当前位置 - 按压位置
                offsetX = event.x - downX
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
            }
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.YELLOW)
        canvas.scale(0.8f, 0.8f, width / 2f, height / 2f)

        // 绘制网格
        drawGrid(canvas)

        // 绘制文字
        drawText(canvas)

        // 绘制点和连接线
        drawPoint(canvas)
    }

    private val originList = listOf(
        70, 80, 100, 222, 60,
        70, 80, 100, 222, 60,
    )

    private val path = Path()

    private fun drawPoint(canvas: Canvas) {
        paint.strokeWidth = 10.dp

        // 数组最大值
        val max = originList.maxOrNull()!!

        // 每一格的宽高
        val eachHeight = height.toFloat() / max
        val eachWidth = width.toFloat() / verticalCount

        originList.forEachIndexed { index, value ->
            val x = eachWidth * index + offsetX
            val y = height - eachHeight * value // 取反
            canvas.drawPoint(x, y, paint)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.dp

        // 裁剪表格， 只保留表格内的数据
        canvas.clipRect(0, 0, width, height)
        paint.color = Color.BLACK
        canvas.drawPath(path, paint)
        path.reset()
    }

    private fun drawText(canvas: Canvas) {

        paint.textSize = 16.dp
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL

        // 获取最大值
        val max = originList.maxOrNull()!!
        // 计算每一格的值
        val eachNumber = max / horizontalCount

        data.forEachIndexed { index, value ->

            // 最大值 - 当前值 = "翻转"数据
            val number = max - eachNumber * index
            // 如果number > 0 并且当前不是最后一行
            val text = "$number"
            // 计算文字宽高
            val rect = Rect()
            paint.getTextBounds(text, 0, text.length, rect)
            val textWidth = rect.width()
            val textHeight = rect.height()

            val x = -textWidth - 5.dp // 不让他贴的太近,在稍微往左一点
            val y = value.y - paint.fontMetrics.top
            canvas.drawText(
                text,
                x,
                y - textHeight,
                paint
            )
        }
    }

    private fun drawGrid(canvas: Canvas) {
        paint.color = BACK_COLOR
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 2.dp

        data.forEach {
            // 绘制垂直线
            canvas.drawLine(
                it.x,
                0f,
                it.x,
                height * 1f,
                paint
            )

            // 绘制平行线
            canvas.drawLine(
                0f,
                it.y,
                width * 1f,
                it.y,
                paint
            )
        }
//        // 绘制右侧线
        canvas.drawLine(
            width * 1f,
            0f,
            width * 1f,
            height * 1f,
            paint
        )

        // 绘制底部线
        canvas.drawLine(
            0f,
            height * 1f,
            width * 1f,
            height * 1f,
            paint
        )
    }
}