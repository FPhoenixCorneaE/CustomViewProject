package com.example.customviewproject.e.e1.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: E1ChartView
 * @Author: 史大拿
 * @CreateDate: 9/16/22$ 2:59 PM$
 * TODO
 */
class E1ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 左侧文字显示个数
        const val LEFT_TEXT_NUMBER = 5

        // 横向最大数量
        const val HORIZONTAL_MAX = 10

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

    private val originList = listOf(
        20, 40, 60, 80, 100,
        70, 80, 100, 222, 60,
        20, 40, 60, 80, 100,
        70, 80, 100, 200, 60,
    )

    private val data = arrayListOf<E1Bean>()

    private val path = Path()

    private val pathMeasure = PathMeasure()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(TAG, "onMeasure")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (data.size == 0) {
//        val eachWidth = w / LEFT_TEXT_NUMBER
            val eachWidth = w / HORIZONTAL_MAX
            val eachHeight = h / LEFT_TEXT_NUMBER
            originList.forEachIndexed { index, _ ->
                data.add(E1Bean(index * eachWidth * 1f, index * eachHeight * 1f))
                Log.i("szjData123", data[index].toString() + "\tindex:$index")
            }
            Log.e(TAG, "onSizeChanged width:$eachWidth\theight:$eachHeight\t")
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.e(TAG, "onLayout")
    }

    override fun onDraw(canvas: Canvas) {
        Log.e(TAG, "onDraw")
//        canvas.drawColor(Color.YELLOW)
        canvas.scale(0.8f, 0.8f, width / 2f, width / 2f)
        // 绘制网格
        drawGrid(canvas)

        // 绘制文字
        drawText(canvas)

        // 绘制点 和 绘制线
        drawPoint(canvas, linePaint)

        canvas.scale(1f, 1f, width / 2f, width / 2f)
        if (!isFlag) {

            startLineAnimator()
            isFlag = !isFlag
        }
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
            val fraction = it.animatedValue as Float
            val dashPathEffect = DashPathEffect(floatArrayOf(length, length), length * fraction)
            linePaint.pathEffect = dashPathEffect
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
//                translationX = event.x
            }
        }

        invalidate()
        return true
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/16/22 4:11 PM
     * TODO 绘制点
     */
    private fun drawPoint(canvas: Canvas, paint: Paint) {
        // 10, 15, 20, 30, 50, 21, 40, 70
        val eachHeight = height.toFloat() / originList.maxOrNull()!!
//        val eachWidth = width.toFloat() / originList.size
        val eachWidth = width.toFloat() / HORIZONTAL_MAX
        paint.strokeWidth = 10.dp
        // 设置圆形状点
        paint.strokeCap = Paint.Cap.ROUND

//        canvas.drawPoint(100.dp,100.dp,paint)
        Log.e(TAG, "offsetX:${offsetX}")

        originList.forEachIndexed { index, value ->
            val x = (eachWidth * index) + offsetX
            val y = height - (eachHeight * value)

            if (x <= width) {
                // 绘制点
                canvas.drawPoint(x, y, paint)
            }
            // 绘制线
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            Log.i(TAG, "point: width:${(eachWidth * index)}\t height:${(eachHeight * value)}")
        }

        // TODO 绘制连接线
        paint.strokeWidth = 2.dp
        paint.style = Paint.Style.STROKE
        pathMeasure.setPath(path, false)
        canvas.drawPath(path, paint)
        path.reset()
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/16/22 4:11 PM
     * TODO 绘制文字
     */
    private fun drawText(canvas: Canvas) {
        paint.textSize = 16.dp
        paint.color = Color.BLACK

        val max = originList.maxOrNull()!!
        val temp = max / LEFT_TEXT_NUMBER

        Log.e("szjTempSize", "${temp}\tsize:${data.size}")
        data.forEachIndexed { index, value ->

            val number = max - temp * index
            Log.e("szj绘制文字", "$number")

            if (number > 0) {
                canvas.drawText(
                    "$number",
                    (-20).dp,
                    value.y,
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
}