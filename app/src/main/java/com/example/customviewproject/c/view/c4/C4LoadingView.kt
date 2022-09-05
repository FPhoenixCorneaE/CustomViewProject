package com.example.customviewproject.c.view.c4

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import com.example.customviewproject.ext.colorRandom
import com.example.customviewproject.ext.dp
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 *
 * @ClassName: C4LoadingView
 * @Author: 史大拿
 * @CreateDate: 8/31/22$ 3:32 PM$
 * TODO
 */
open class C4LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 小圆半径
        val SMALL_RADIUS = 10.dp

        // 旋转时间
        const val ROTATION_TIME = 5000L
    }

    private val paint = Paint()

    private val data = arrayListOf(
        Color.BLACK,
        Color.RED,
        Color.YELLOW,
        Color.GRAY,
        Color.LTGRAY,
        Color.RED,
        colorRandom,
        colorRandom,
        colorRandom,
    )

    // 大圆半径
    var bigRadius = 100.dp

    // 弧度
    var angle = 0.0f
        set(value) {
            field = value
            invalidate()
        }

    lateinit var currentState: LoadingViewState

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 默认旋转状态
        currentState = RotateState()

        // 3秒后改变为缩放状态
        postDelayed({
            currentState.close()
            currentState = ZoomState()
        }, 2000)
    }

    override fun onDraw(canvas: Canvas) {
        currentState.onDraw(canvas)
    }

    // 旋转状态
    inner class RotateState : LoadingViewState {
        // 弧度动画
        private val angleAnimator by lazy {
            ObjectAnimator.ofFloat(this@C4LoadingView, "angle", 360f).apply {
                repeatCount = -1 // 无限重复
                duration = ROTATION_TIME
                // 默认插值器为 AccelerateDecelerateInterpolator() 修改成更平和的Linear迭代器
                interpolator = null
//                repeatMode = ValueAnimator.REVERSE
            }
        }

        init {
            angleAnimator.start()
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawColor(Color.WHITE)
            data.forEachIndexed { _, value ->
                paint.color = value

                // 每一次改变角度
                angle += 360 / data.size - 1

                // 计算dx dy
                val dx = bigRadius * sin(Math.toRadians(angle.toDouble())).toFloat() + width / 2
                val dy =
                    bigRadius * cos(Math.toRadians(angle.toDouble())).toFloat() + height / 2

                canvas.drawCircle(dx, dy, SMALL_RADIUS, paint)
            }
        }

        override fun close() {
            angleAnimator.removeAllListeners()
        }
    }

    // 手动关闭
    open fun close() {
        currentState.close()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        close()
    }


    // 缩放状态
    inner class ZoomState : LoadingViewState {
        private val bigRadiusAnimator by lazy {
            ObjectAnimator.ofFloat(this@C4LoadingView, "bigRadius", 0f).apply {
                duration = 4000
                interpolator = AnticipateInterpolator(5f)
            }
        }

        init {
            bigRadiusAnimator.start()
            bigRadiusAnimator.doOnEnd {
                currentState.close()
                currentState = DiffusionState()
            }
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawColor(Color.WHITE)
            data.forEach {
                paint.color = it
                // 计算dx dy
                val dx = bigRadius * sin(Math.toRadians(angle.toDouble())).toFloat() + width / 2
                val dy =
                    bigRadius * cos(Math.toRadians(angle.toDouble())).toFloat() + height / 2

                canvas.drawCircle(dx, dy, SMALL_RADIUS, paint)
                // 每一次改变角度
                angle += 360 / data.size - 1
            }
        }

        override fun close() {
            bigRadiusAnimator.removeAllListeners()
        }
    }

    // 扩散效果
    inner class DiffusionState : LoadingViewState {

        private var radius = 0f
            set(value) {
                field = value

                // 画笔宽度 = 对角线 - 当前值
                paint.strokeWidth = diagonal - value

                invalidate()
            }

        // 对角线长度
        private val diagonal = sqrt((width + height).toDouble().pow(2.0)).toFloat() / 2

        init {
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE


        }

        private val radiusAnimator = ObjectAnimator.ofFloat(this, "radius", diagonal).apply {
            duration = 2000
            start()
        }


        override fun onDraw(canvas: Canvas) {

            /**
             * 假设当前 paint.strokeWidth = 100; 圆的半径 = 300
             * 那么圆有颜色的区域为 250-350之间，这块区域指的是strokeWidth
             * 而不是 300-400 或者200-300 之间
             *
             * 所以当前圆的半径为: 画笔的一半 + 已经画过的地方
             */
            val radius = paint.strokeWidth / 2 + radius
            Log.e("szjRadius", "$radius\tstrokeWidth:${paint.strokeWidth}")
            canvas.drawCircle(width / 2f, height / 2f, radius, paint)
        }

        override fun close() {
            radiusAnimator.removeAllListeners()
        }
    }

    interface LoadingViewState {
        fun onDraw(canvas: Canvas)
        fun close()
    }
}