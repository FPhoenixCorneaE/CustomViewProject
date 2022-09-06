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
import com.example.customviewproject.util.bezier.SecondListBezierTypeEvaluator
import kotlin.math.cos
import kotlin.math.sin


/**
 *
 * @ClassName: FireworksView
 * @Author: 史大拿
 * @CreateDate: 9/6/22$ 9:26 AM$
 * TODO
 */
class FireworksView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 半径
        val RADIUS = 200.dp

        // 总个数
        const val COUNT = 100

        // 动画播放时间
        const val TIME = 5000L

        const val TEXT = "中秋节快乐!"

        // 字体大小
        val TEXT_SIZE = 36.dp

        // 渐变宽度
        val GRADIENT_WIDTH = 60.dp
    }

    // 控制点
    private val controlPointF = PointF(100.dp, 100.dp)

    // 开始点
    private val startPointF by lazy { PointF(width / 2f, height / 2f) }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.strokeWidth = 2.dp
        it.color = Color.BLACK
        it.style = Paint.Style.STROKE
    }

    val path = Path()

    var points = arrayListOf<PointF>()
        set(value) {
            field = value
            Log.e("szjPoints", "${field.size}")
            repeat(COUNT) {
                paths[it].second.lineTo(value[it].x, value[it].y)
            }
            invalidate()
        }

    private var textWidthShader = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val textAnimator by lazy {
        // 文字宽度
        val textWidth = paint.measureText(TEXT)
        val x = startPointF.x - textWidth / 2f
        ObjectAnimator.ofFloat(this, "textWidthShader", textWidth + x).apply {
            duration = TIME
            repeatMode = ValueAnimator.REVERSE // 翻转执行
            repeatCount = -1 // 重复执行
        }
    }


    private val paths = arrayListOf<Pair<Int, Path>>()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        secondListBezierAnimator()

        textAnimator.start()
    }

    private fun secondListBezierAnimator() {
        val p0 = arrayListOf<PointF>() // 开始点
        val p1 = arrayListOf<PointF>() // 控制点
        val p2 = arrayListOf<PointF>() // 结束点
        var angle = 0.0
        repeat(COUNT) {
            p0.add(startPointF)
            p1.add(controlPointF)
            val x = RADIUS * sin(Math.toRadians(angle)) + startPointF.x
            val y = RADIUS * cos(Math.toRadians(angle)) + startPointF.y
            p2.add(PointF(x.toFloat(), y.toFloat()))

            // 一个的角度
            angle += 360.0 / COUNT

            val path = Path()
            path.moveTo(p0[it].x, p0[it].y)

            paths.add(colorRandom to path)
        }
        val animator = ObjectAnimator.ofObject(
            this,
            "points",
            SecondListBezierTypeEvaluator(p1),
            p0,
            p2
        )
        animator.duration = TIME
        animator.start()
    }


//    private val paths by lazy {
//
//        val list = arrayListOf<Path>()
////        var path: Path
////
////        repeat(COUNT) {
////            path = Path()
////            path.moveTo(startPointF.x, startPointF.y)
////            val x = RADIUS * sin(Math.toRadians(angle)) + width / 2f
////            val y = RADIUS * cos(Math.toRadians(angle)) + height / 2f
//////            path.lineTo(x.toFloat(), y.toFloat())
////
////            val cX = 200.dp
////            val cY = 200.dp
////            path.quadTo(cX, cY, x.toFloat(), y.toFloat())
////
////            // 一个的角度
////            angle += 360 / COUNT
////            list.add(path)
////        }
//        list
//    }


    override fun onDraw(canvas: Canvas) {
//        canvas.drawPath(path, paint)

        paint.style = Paint.Style.STROKE
        repeat(paths.size) {
            paint.color = paths[it].first
            canvas.drawPath(paths[it].second, paint)
        }


        // 绘制文字
        drawText(canvas)

    }

    /*
     * 作者:史大拿
     * 创建时间: 9/6/22 1:44 PM
     * TODO 绘制文字
     */
    private fun drawText(canvas: Canvas) {
        paint.textSize = TEXT_SIZE
        paint.style = Paint.Style.FILL

        // 文字宽度
        val textWidth = paint.measureText(TEXT)

        val x = startPointF.x - textWidth / 2f
        val y = -paint.fontMetrics.top + 50.dp

        val colors = intArrayOf(Color.BLACK, Color.RED, Color.YELLOW, Color.TRANSPARENT)
        val linearGradient = LinearGradient(
            0f + x,
            0f + y,
            x + GRADIENT_WIDTH,
            0f + y,
            colors,
            null,
            Shader.TileMode.CLAMP
        )

        // 设置矩阵
//        val matrix = Matrix()
//        matrix.setTranslate(textWidthShader, 0f)
//        linearGradient.setLocalMatrix(matrix)
        linearGradient.transform {
            setTranslate(textWidthShader, 0f)
        }

        // 设置渐变颜色
        paint.shader = linearGradient
        canvas.drawText(
            TEXT, 0, TEXT.length,
            x, y,
            paint
        )
        paint.shader = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        textAnimator.removeAllListeners()
    }

}