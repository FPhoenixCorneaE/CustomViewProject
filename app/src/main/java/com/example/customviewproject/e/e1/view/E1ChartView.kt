package com.example.customviewproject.e.e1.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withSave
import com.example.customviewproject.e.e1.view.E1ChartView.LineStyle.*
import com.example.customviewproject.e.e1.view.E1ChartView.PointStyle.*
import com.example.customviewproject.ext.contains
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: E1ChartView
 * @Author: 史大拿
 * @CreateDate: 9/16/22$ 2:59 PM$
 * TODO
 */
open class E1ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {


        const val TAG = "E1ChartView"

        // 网格颜色
        val BACK_COLOR = Color.parseColor("#88888888")
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.strokeWidth = 1.dp
    }

    // 线段画笔
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.strokeWidth = 1.dp
    }

    // 水平个数 左侧文字显示个数
    private val horizontalCount: Int
        get() {
            return adapter?.horizontalCount() ?: 5
        }

    // 垂直个数
    private val verticalCount: Int
        get() {
            return adapter?.verticalCount() ?: 5
        }

    // 原始数据
    open var originList = arrayListOf<Int>()

    private val data = arrayListOf<E1LocationBean>()

    private val path = Path()

    private val pathMeasure = PathMeasure()

    // 当前点的样式
    open val currentPointStyle: PointStyle
        get() {
            return adapter?.pointStyle() ?: ROUND
        }

    // 当前矩形样式
    open val currentFillStyle: FillStyle
        get() {
            return adapter?.fillStyle() ?: FillStyle.STROKE
        }

    // 当前绘制线样式
    open val currentLineStyle: LineStyle
        get() {
            return adapter?.lineStyle() ?: SOLID_LINE
        }

    // 是否绘制网格
    private val isDrawGrid: Boolean
        get() {
            return adapter?.isDrawGrid() ?: true
        }

    open var adapter: BaseChatAdapter? = null
        set(value) {
            field = value
            if (field == null) {
                throw NullPointerException("adapter = NULL")
            }
        }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (data.size == 0) {
//        val eachWidth = w / LEFT_TEXT_NUMBER
            val eachWidth = w / verticalCount
            val eachHeight = h / horizontalCount


            originList.forEachIndexed { index, value ->
                data.add(
                    E1LocationBean(
                        index * eachWidth * 1f,
                        index * eachHeight * 1f,
                        value
                    )
                )
            }
            Log.e(TAG, "onSizeChanged width:$eachWidth\theight:$eachHeight\t")
        }
    }


    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.YELLOW)
        canvas.scale(0.8f, 0.8f, width / 2f, width / 2f)
        if (isDrawGrid) {
            // 绘制网格
            drawGrid(canvas)
        }


        // 绘制文字
        drawText(canvas)


        // 因为绘制线需要裁剪，所以保存一下画布
        canvas.withSave {
            // 绘制线
            drawLine(canvas, linePaint)
        }

        // 绘制点
        drawPoint(canvas, linePaint)

//        canvas.scale(currentZoom, currentZoom, width / 2f, width / 2f)

        // 这里这么写不太好，应该是设置数据后调用，毕竟是入门，就不改了。。。
        if (!isFlag) {
            startLineAnimator()
            isFlag = !isFlag
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/17/22 2:41 PM
     * TODO 绘制线
     */
    private fun drawLine(canvas: Canvas, paint: Paint) {
        val eachHeight = eachHeight
        val eachWidth = eachWidth

        originList.forEachIndexed { index, value ->
            val x = ((eachWidth * index) + offsetX)
            val y = (height - (eachHeight * value))
            // 绘制线
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        // 裁剪矩形区域，保证线在屏幕内
        canvas.clipRect(0, 0, width, height)

        if (currentFillStyle == FillStyle.FILL) {
            // TODO 实心
            path.lineTo(width.toFloat(), height.toFloat())
            path.lineTo(0f, height.toFloat())
            path.close()
            paint.style = Paint.Style.FILL
            paint.shader = LinearGradient(
                width * 1f,
                height / 2f,
                width * 1f,
                height * 1f,
                Color.RED,
                Color.YELLOW,
                Shader.TileMode.CLAMP
            )
            canvas.drawPath(path, paint)
            paint.shader = null
        } else {
            // TODO 空心
            paint.strokeWidth = 2.dp
            paint.style = Paint.Style.STROKE
            canvas.drawPath(path, paint)
        }

        paint.style = Paint.Style.STROKE
        pathMeasure.setPath(path, false)
        path.reset()
    }

    private var isFlag = false

    /*
     * 作者:史大拿
     * 创建时间: 9/16/22 4:35 PM
     * TODO 开启绘制线的动画
     */
    private fun startLineAnimator() {
        val animator = ObjectAnimator.ofFloat(1f, 0f)
        animator.duration = 2000
        val length = pathMeasure.length

        animator.addUpdateListener {

            // 当前进度
            val fraction = it.animatedValue as Float

            // 画实线
            val dashPathEffect1 = DashPathEffect(floatArrayOf(length, length), length * fraction)

            linePaint.pathEffect = when (currentLineStyle) {
                DOTTED_LINE -> {
                    // 画虚线
                    val dashPathEffect2 = DashPathEffect(floatArrayOf(20f, 10f), 0f)
                    ComposePathEffect(dashPathEffect2, dashPathEffect1)
                }
                SOLID_LINE -> dashPathEffect1
            }

//            Log.e(TAG, "dashPathEffect:${length * fraction}\t$dashPathEffect")
            invalidate()
        }
        animator.start()
    }

    private var offsetX = 0f
    private var downX = 0f
    private var originX = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x

                originX = offsetX
            }

            MotionEvent.ACTION_MOVE -> {
                // 当前偏移位置 = 当前位置 - 按压位置 + 原始偏移量
                offsetX = event.x - downX + originX

                //
                if (offsetX > 0) {
                    offsetX = 0f
                }

                if (offsetX <= -(data.last().x - width)) {
                    offsetX = -(data.last().x - width)
                }

                data.forEachIndexed { index, value ->

                    if (PointF(event.x - offsetX, 0f).contains(
                            PointF(value.x, 0f),
                            eachWidth / 2f
                        )
                    ) {
                        drawHintTextPosition = index
                        Log.e("szj选择了", "index:${index}")
                    }
                }

            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                drawHintTextPosition = -1
            }
        }

        invalidate()
        return true
    }

    // 每一格的高
    private val eachHeight: Float
        get() {
            return height.toFloat() / originList.maxOrNull()!!
        }

    // 每一格的宽
    private val eachWidth: Float
        get() {
            return width.toFloat() / verticalCount
        }


    // 绘制提示文字下标
    private var drawHintTextPosition = -1

    /*
     * 作者:史大拿
     * 创建时间: 9/16/22 4:11 PM
     * TODO 绘制点
     */
    private fun drawPoint(canvas: Canvas, paint: Paint) {
        // 10, 15, 20, 30, 50, 21, 40, 70
        val eachHeight = eachHeight
        val eachWidth = eachWidth
        paint.strokeWidth = 10.dp

        // 设置圆形状点
        paint.strokeCap = when (currentPointStyle) {
            ROUND -> Paint.Cap.ROUND
            SQUARE -> Paint.Cap.SQUARE
        }

        originList.forEachIndexed { index, value ->
            val x = ((eachWidth * index) + offsetX)
            val y = height - (eachHeight * value)

            // 在屏幕内才绘制点
            if (x >= 0 && x <= width) {
                if (drawHintTextPosition != -1
//                    && index == drawHintTextPosition
                ) {
                    paint.textSize = 16.dp
                    paint.style = Paint.Style.FILL
                    val text = "${data[index].number}"
                    val textWidth = paint.measureText(text)
                    val textX = x - textWidth / 2f
                    val textY = y - paint.fontMetrics.top
                    // 需要绘制文字
                    canvas.drawText(text, textX, textY, paint)
                } else {
                    // 绘制点
                    canvas.drawPoint(x, y, paint)
                }
            }
        }
    }


    /*
     * 作者:史大拿
     * 创建时间: 9/16/22 4:11 PM
     * TODO 绘制文字
     */
    private fun drawText(canvas: Canvas) {
        paint.textSize = 16.dp
        paint.color = Color.BLACK
        // 获取最大值
        val max = originList.maxOrNull()!!
        // 计算每一格的值
        val eachNumber = max / horizontalCount

        data.forEachIndexed { index, value ->
            val number = max - eachNumber * index
            Log.e("szj绘制文字", "number:$number\tindex:$index\tcount:${verticalCount}")

            // 如果number > 0 并且当前不是最后一行
            if (number >= 0 && index != horizontalCount) {
                val text = "$number"
                val rect = Rect()

                // 计算文字宽高
                paint.getTextBounds(text, 0, text.length, rect)
                val textWidth = rect.width()
                val textHeight = rect.height()

                val x = -textWidth - 5.dp
                val y = value.y - paint.fontMetrics.top
                canvas.drawText(
                    text,
                    x,
                    y - textHeight,
                    paint
                )
            }
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/16/22 3:19 PM
     * TODO 绘制网格
     */
    private fun drawGrid(canvas: Canvas) {
        paint.color = BACK_COLOR

        data.forEach {
            Log.e("szjData", it.toString())

            canvas.drawLine(
                it.x,
                0f,
                it.x,
                height * 1f,
                paint
            )

            if (it.y <= height) {
                canvas.drawLine(
                    0f,
                    it.y,
                    width * 1f,
                    it.y,
                    paint
                )
            }

        }
        canvas.drawLine(
            width * 1f,
            0f,
            width * 1f,
            height * 1f,
            paint
        )

        canvas.drawLine(
            0f,
            height * 1f,
            width * 1f,
            height * 1f,
            paint
        )
    }


    // 填充样式
    enum class FillStyle {
        /*
         * 作者:史大拿
         * 创建时间: 9/17/22 1:51 PM
         * TODO 空心
         */
        STROKE,

        /*
         * 作者:史大拿
         * 创建时间: 9/17/22 1:51 PM
         * TODO 实心
         */
        FILL
    }

    // 点的样式
    enum class PointStyle {
        /*
         * 作者:史大拿
         * 创建时间: 9/17/22 1:52 PM
         * TODO 圆点
         */
        ROUND,

        /*
         * 作者:史大拿
         * 创建时间: 9/17/22 1:53 PM
         * TODO 方形
         */
        SQUARE
    }

    enum class LineStyle {
        /*
         * 作者:史大拿
         * 创建时间: 9/17/22 2:33 PM
         * TODO 实线
         */
        SOLID_LINE,

        /*
         * 作者:史大拿
         * 创建时间: 9/17/22 2:33 PM
         * TODO 虚线
         */
        DOTTED_LINE
    }
}