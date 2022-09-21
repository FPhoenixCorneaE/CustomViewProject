package com.example.customviewproject.e.e2

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.OverScroller
import androidx.core.graphics.withSave
import androidx.core.view.GestureDetectorCompat
import com.example.customviewproject.e.BaseChartView

/**
 *
 * @ClassName: E2RectChartView
 * @Author: 史大拿
 * @CreateDate: 9/21/22$ 11:12 AM$
 * TODO
 */
class E2RectChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseChartView(context, attrs, defStyleAttr), Runnable {
    companion object {
        const val TAG = "E2RectChartView"

        // 矩形宽度比率 [0f .. 1f]
        const val RECT_WIDTH_RATIO = 0.6f

        // 动画时间
        const val ANIMATOR_DURATION = 2000L
    }

    private var currentFraction = 0f

    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 2:04 PM
     * TODO 入场动画
     */
    private val admissionAnimation by lazy {
        val animator = ObjectAnimator.ofFloat(0f, 1f)
        animator.duration = ANIMATOR_DURATION
        animator.addUpdateListener {
            currentFraction = it.animatedValue as Float
            invalidate()
        }
        animator
    }

    init {
        originList = arrayListOf(
            148, 120, 21, 60, 10,
            70, 80, 100, 222, 74,
        )

        admissionAnimation.start()
    }

    override var horizontalCount: Int = 10
    override var verticalCount: Int = 5

    private var offsetX = 0f
    private var downX = 0f
    private var originX = 0f

    // 手势监听
    private val gestureDetectorCompat = GestureDetectorCompat(context, E2RectGesture())

    // 用来设置fling
    private val overScroll = OverScroller(context)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetectorCompat.onTouchEvent(event)

        return true
    }

    // 每一格矩形的宽
    private val eachWidth: Float
        get() {
            return width / verticalCount.toFloat()
        }

    override fun onDraw(canvas: Canvas) {
        canvas.scale(0.8f, 0.8f, width / 2f, width / 2f)
        // 绘制表格
        super.drawGrid(canvas)

        // 绘制左侧文字
        super.drawLeftText(canvas)


        // 每一格的矩形宽度
        val eachWidth = eachWidth
        // 矩形的宽度
        val rectWidth = width / verticalCount * RECT_WIDTH_RATIO

        // 计算出每一格的高度
        val eachGrid = height.toFloat() / originMax

        canvas.withSave {
            // 裁剪
            canvas.clipRect(0, 0, width, height)
            // 绘制矩形
            drawRect(canvas, eachWidth, rectWidth, eachGrid)
        }

        // 绘制矩形文字
        drawText(canvas, eachWidth, eachGrid)

    }

    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 2:11 PM
     * TODO 绘制文字
     * @param eachWidth: 每一格的矩形宽度
     * @param eachGridHeight: 每一格矩形的高度
     */
    private fun drawText(canvas: Canvas, eachWidth: Float, eachGridHeight: Float) {
        var tempX = eachWidth / 2f
        originList.forEachIndexed { _, value ->
            val text = String.format("%.0f", value * currentFraction)

            // 文字的宽
            val textWidth = paint.measureText(text)

            val x = tempX - textWidth / 2f + offsetX
            val y = (height - eachGridHeight * value * currentFraction) - paint.fontMetrics.bottom

            // 如果在范围内才绘制
            if (x >= 0 && x <= width) {
                canvas.drawText(text, x, y, paint)
            }

            tempX += eachWidth

        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 2:10 PM
     * TODO 绘制矩形
     * @param eachWidth: 每一格的矩形宽度
     * @param rectWidth: 每一个矩形的宽
     * @param eachGridHeight: 每一格矩形的高度
     */
    private fun drawRect(
        canvas: Canvas,
        eachWidth: Float,
        rectWidth: Float,
        eachGridHeight: Float
    ) {

        Log.e(TAG, "$eachGridHeight\tmax:$originMax")
        var tempLeft = eachWidth / 2f - rectWidth / 2f

        originList.forEach {

            val left = tempLeft + offsetX
            // 从上置下动画
//            val top = eachGrid * (originMax - it) * currentFraction
            // 从下置上动画
            val top = height - eachGridHeight * it * currentFraction
            val right = left + rectWidth
            val bottom = height.toFloat()

            Log.e(
                TAG,
                "value:${it}\t eachGrid:${eachGridHeight}\tleft:$left\ttop:$top\tright:$right\tbottom:$bottom"
            )
            canvas.drawRoundRect(left, top, right, bottom, 0f, 0f, paint)
            tempLeft += eachWidth
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 2:38 PM
     * TODO 手势监听
     */
    private inner class E2RectGesture : GestureDetector.OnGestureListener {
        override fun onDown(event: MotionEvent): Boolean {
            Log.e("E2RectGesture", "onDown")
            downX = event.x
            originX = offsetX
            return true
        }

        override fun onShowPress(e: MotionEvent?) {
            Log.e("E2RectGesture", "onShowPress")
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.e("E2RectGesture", "onSingleTapUp")
            return true
        }

        override fun onScroll(
            e1: MotionEvent,// down按下的位置
            event: MotionEvent, // 当前滑动的事件
            distanceX: Float, // event - e1 的X距离
            distanceY: Float // event - e2 的Y距离
        ): Boolean {
            Log.e("E2RectGesture", "e1:{x${e1.x},y:${e1.y}}\te2:{x${event.x},y:${event.y}}")
            when (event.action) {

                MotionEvent.ACTION_MOVE -> {
                    // 当前偏移位置 = 当前位置 - 按压位置 + 原始偏移量
                    offsetX = event.x - downX + originX

                    // 修复X轴距离
                    repairOffSetX()
                }
            }

            invalidate()
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            Log.e("E2RectGesture", "onLongPress")
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val startX = offsetX.toInt()
            val startY = 0

            val vX = velocityX.toInt()
            val vY = 0

            val minX = -(originList.size * eachWidth - width).toInt()
            val maxX = 0

            val minY = 0
            val maxY = 0

            Log.e(TAG, "onFling:minX:$minX\tvX:$vX")
            overScroll.fling(
                startX, startY,
                vX, vY,
                minX, maxX,
                minY, maxY,
                0, 0
            )

            postOnAnimation(this@E2RectChartView)
            return true
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 3:05 PM
     * TODO 修复X轴距离
     */
    private fun repairOffSetX() {
        // 限制滑动距离
        if (offsetX > 0) {
            offsetX = 0f
        }

        if (offsetX <= -(originList.size * eachWidth - width)) {
            offsetX = -(originList.size * eachWidth - width)
        }
    }

    override fun run() {
        // 计算偏移量 返回是否完成
        val isOver = overScroll.computeScrollOffset()
        if (isOver) {
            offsetX = overScroll.currX.toFloat()
            repairOffSetX() // 修复X轴距离
            invalidate()
            postOnAnimation(this) //
        }
    }
}