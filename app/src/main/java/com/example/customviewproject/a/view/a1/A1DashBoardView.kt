package com.example.customviewproject.a.view.a1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.customviewproject.ext.szj.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @ClassName: DashBoardView
 * @Author: 史大拿
 * @CreateDate: 8/5/22$ 7:13 PM$
 * TODO 仪表盘View
 */
class A1DashBoardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE // 空心
        strokeWidth = 4f
    }

    companion object {

        // 仪表盘半径
        private val RADIUS = 100f.dp

        // 仪表盘角度
        private const val ANGLE = 120f

        // 指针宽高
        private val POINTER = Pair(6f, 16f)

        // 指针最大个数
        private const val POINTER_MAX_COUNT = 25f

        // 箭头宽高
        private val ARROW_LENGTH = 80f.dp

        // 当前箭头位置
        private var CURRENT_POSITION = 2

    }

    // 屏幕宽高
    private var currentWidth = 0f
    private var currentHeight = 0f

    private lateinit var pathMeasure: PathMeasure

    private val dashPath = Path()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        currentWidth = w.toFloat()
        currentHeight = h.toFloat()
        // 小矩形
        dashPath.addRect(RectF(0f, 0f, POINTER.first, POINTER.second), Path.Direction.CCW)


    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO 绘制扇形小锯齿(指针)
        drawSawtooth(canvas)

        // TODO 绘制弧形
        drawArc(canvas)

        // TODO 绘制箭头
        drawArrow(canvas)
    }

    // 绘制指针
    private fun drawArrow(canvas: Canvas) {

        val start = ANGLE / 2f + 90
        // 每一个刻度的角度
        val angle = (360 - ANGLE) / POINTER_MAX_COUNT

        // 当前角度 = 每一个刻度的角度 * 当前位置 + 开口位置
        val currentAngle = angle * CURRENT_POSITION + start

        canvas.drawLine(currentWidth / 2f,
            currentHeight / 2f,
            currentWidth / 2f + ARROW_LENGTH * cos(Math.toRadians((currentAngle).toDouble())).toFloat(),
            currentHeight / 2f + ARROW_LENGTH * sin(Math.toRadians((currentAngle).toDouble())).toFloat(),
            paint)
    }

    // 绘制弧形
    private fun drawArc(canvas: Canvas) {
        // 将路径置空
        paint.pathEffect = null
        // 再次绘制扇形弧形
        canvas.drawArc(
            RectF(
                currentWidth / 2f - RADIUS,
                currentHeight / 2f - RADIUS,
                currentWidth / 2f + RADIUS,
                currentHeight / 2f + RADIUS,
            ),
            ANGLE / 2 + 90,
            360 - ANGLE,
            false,
            paint
        )
    }

    // 绘制锯齿
    private fun drawSawtooth(canvas: Canvas) {
        val path = Path()
        path.addArc(
            RectF(
                currentWidth / 2f - RADIUS,
                currentHeight / 2f - RADIUS,
                currentWidth / 2f + RADIUS,
                currentHeight / 2f + RADIUS,
            ),
            ANGLE / 2 + 90,
            360 - ANGLE,
        )

        /*
         * @param 1: 需要测量的路径
         * @param forceClosed: 是否需要测量闭合路径
         */
        pathMeasure = PathMeasure(path, false)
        Log.i("szjLength", pathMeasure.length.toString())
        /*
         * 作者:史大拿
         * @param 1: 路径样式
         * @param advance: 路径距离
         * @param phase: 省略多长
         * @param style: 绘制样式
         */
        paint.pathEffect = PathDashPathEffect(dashPath,
            (pathMeasure.length - POINTER.first) / POINTER_MAX_COUNT,
            0f,
            PathDashPathEffect.Style.MORPH)

        canvas.drawPath(path, paint)
    }
}