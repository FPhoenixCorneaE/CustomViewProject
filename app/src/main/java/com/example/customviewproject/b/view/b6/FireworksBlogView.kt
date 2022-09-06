package com.example.customviewproject.b.view.b6


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.transform
import com.example.customviewproject.ext.colorRandom
import com.example.customviewproject.ext.dp
import com.example.customviewproject.util.bezier.SecondBezierTypeEvaluator
import com.example.customviewproject.util.bezier.SecondListBezierTypeEvaluator
import kotlin.math.cos
import kotlin.math.sin


/**
 *
 * @ClassName: FireworksBlogView
 * @Author: 史大拿
 * @CreateDate: 9/6/22$
 * TODO
 */
class FireworksBlogView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.strokeWidth = 2.dp
        it.color = Color.BLACK
        it.style = Paint.Style.STROKE
    }

    var pointF = PointF()
        set(value) {
            field = value
            // 画线
            path.lineTo(value.x, value.y)
            invalidate()
        }
    val path = Path()

    companion object {
        // 总数
        const val COUNT = 100

        // 半径
        val RADIUS = 200.dp
    }

    // 开始点
    private val startPointF by lazy { PointF(width / 2f, height / 2f) }

    // 控制点
    private val controlPointF = PointF(100.dp, 100.dp)

    // 用来存储路径 first画笔颜色, second:路径
    private val paths = arrayListOf<Pair<Int, Path>>()

    var points = arrayListOf<PointF>()
        set(value) {
            field = value
            repeat(COUNT) {
                // 绘制每一条曲线
                paths[it].second.lineTo(value[it].x, value[it].y)
            }
            invalidate()
        }

    private fun secondListBezierAnimator() {
        val p0 = arrayListOf<PointF>() // 开始点
        val p1 = arrayListOf<PointF>() // 控制点
        val p2 = arrayListOf<PointF>() // 结束点
        var angle = 0.0
        // 循环所有的点
        repeat(COUNT) {
            p0.add(startPointF)
            p1.add(controlPointF)
            val x = FireworksView.RADIUS * sin(Math.toRadians(angle)) + width / 2f
            val y = FireworksView.RADIUS * cos(Math.toRadians(angle)) + height / 2f
            p2.add(PointF(x.toFloat(), y.toFloat()))

            // 一个的角度
            angle += 360.0 / FireworksView.COUNT

            val path = Path()
            // 将画笔移动到开始点
            path.moveTo(p0[it].x, p0[it].y)
            // 保存起来
            paths.add(colorRandom to path)
        }
        val animator = ObjectAnimator.ofObject(
            this,
            "points",
            SecondListBezierTypeEvaluator(p1),
            p0,
            p2
        )
        animator.duration = FireworksView.TIME
        animator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        animator()
        secondListBezierAnimator()

        textAnimator.start()
    }

//    private fun animator() {
//        val p0 = PointF(50.dp, 100.dp) // 开始点
//        val p1 = PointF(100.dp, 50.dp) // 控制点
//        val p2 = PointF(150.dp, 100.dp) // 结束点
//        val animator = ObjectAnimator.ofObject(
//            this,
//            "pointF",
//            SecondBezierTypeEvaluator(p1),
//            p0,
//            p2
//        )
//        // 将画笔移动到开始位置
//        path.moveTo(p0.x, p0.y)
//        animator.duration = 2000L // 设置时间
//        animator.start()
//    }


    override fun onDraw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        // 绘制每一条线
        repeat(COUNT) {
            // 设置颜色
            paint.color = paths[it].first
            // 画曲线
            canvas.drawPath(
                paths[it].second, paint
            )
        }

        drawText(canvas)

        //region  画直线
        /*
         * 作者:史大拿
         * 创建时间: 9/6/22 3:20 PM
         */
//        var angle = 0.0
//        repeat(COUNT) {
//            val x = RADIUS * cos(Math.toRadians(angle)) + startPointF.x
//            val y = RADIUS * sin(Math.toRadians(angle)) + startPointF.y
//            canvas.drawLine(
//                startPointF.x, startPointF.y,
//                x.toFloat(), y.toFloat(), paint
//            )
//            angle += 360.0 / COUNT
//        }
        //endregion

        // TODO 画一条
//        canvas.drawPath(path, paint)

    }

    private var textWidthShader = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val textAnimator by lazy {
        // 文字宽度
        val textWidth = paint.measureText(FireworksView.TEXT)
        // 开始位置 = 开始点.x - 文字宽度的一半
        val x = startPointF.x - textWidth / 2f
        ObjectAnimator.ofFloat(
            this, "textWidthShader",
            textWidth + x
        ).apply {
            duration = FireworksView.TIME
            repeatMode = ValueAnimator.REVERSE // 翻转执行
            repeatCount = -1 // 重复执行
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/6/22 3:31 PM
     * TODO 绘制文字
     */
    private fun drawText(canvas: Canvas) {
        paint.textSize = FireworksView.TEXT_SIZE
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK

        // 文字宽度
        val textWidth = paint.measureText(FireworksView.TEXT)

        val x = width / 2f - textWidth / 2f
        // 最底部绘制
        val y = -paint.fontMetrics.top + 50.dp

        // 渐变颜色
        val colors = intArrayOf(Color.BLACK, Color.RED, Color.YELLOW, Color.BLACK)
        // 线性渐变
        val linearGradient = LinearGradient(
            x, // 开始位置
            0f,
            x + FireworksView.GRADIENT_WIDTH, // 渐变的位置
            0f,
            colors,
            null,
            Shader.TileMode.CLAMP
        )

        // 使用ktx扩展
        linearGradient.transform {
            setTranslate(textWidthShader, 0f)
        }

        paint.shader = linearGradient
        canvas.drawText(
            FireworksView.TEXT, 0, FireworksView.TEXT.length,
            x, y, paint
        )
        paint.shader = null
    }

}