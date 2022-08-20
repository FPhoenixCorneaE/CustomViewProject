package com.example.customviewproject.c.view.c2.blog

import android.animation.ObjectAnimator
import android.animation.PointFEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.graphics.minus
import com.example.customviewproject.R
import com.example.customviewproject.c.view.c2.C2View
import com.example.customviewproject.ext.contains
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap
import kotlin.math.*

/**
 *
 * @ClassName: TempView
 * @Author: 史大拿
 * @CreateDate: 8/19/22$ 7:05 PM$
 * TODO 博客地址:https://blog.csdn.net/weixin_44819566
 */
class DragView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 大圆半径
        private val BIG_RADIUS = 20.dp

        // 小圆半径
        private val SMALL_RADIUS = BIG_RADIUS

        // 最大范围(半径)，超出这个范围大圆不显示
        private val MAX_RADIUS = 150.dp
    }

    private val paint = Paint().apply {
        color = Color.RED
    }

    // 大圆初始位置
    var bigPointF = PointF(0f, 0f)
        set(value) {
            field = value
            invalidate()
        }

    // 小圆初始位置
    private val smallPointF = PointF(0f, 0f)


    // 更新移动点 (大圆位置)
    fun upDataPointF(x: Float, y: Float) {
        bigPointF.x = x
        bigPointF.y = y
        invalidate()
    }


    // 初始化起始点 (小圆位置)
    fun initPointF(x1: Float, y1: Float, x2: Float, y2: Float) {
        // 小圆位置
        smallPointF.x = x1
        smallPointF.y = y1

        Log.e("szj大圆位置:","x=${x2},y=${y2}")
        // 大圆位置
        bigPointF.x = x2
        bigPointF.y = y2
        invalidate()
    }


    private val explodeImages by lazy {
        val list = arrayListOf<Bitmap>()
        val width = bitMapWidth
        list.add(getBitMap(R.mipmap.explode_0, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_1, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_2, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_3, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_4, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_5, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_5, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_6, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_7, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_8, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_9, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_10, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_11, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_12, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_13, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_14, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_15, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_16, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_17, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_18, width.toInt()))
        list.add(getBitMap(R.mipmap.explode_19, width.toInt()))
        list
    }

    // 爆炸下标
    var explodeIndex = -1
        set(value) {
            field = value
            invalidate()
        }


    // 爆炸效果动画
    val explodeAnimator by lazy {
        ObjectAnimator.ofInt(this, "explodeIndex", 19, -1).apply {
            duration = 1000
        }
    }

    // 大圆回到小圆位置动画
    fun bigAnimator(): ValueAnimator {
        return ObjectAnimator.ofObject(this, "bigPointF", PointFEvaluator(),
            PointF(smallPointF.x, smallPointF.y)).apply {
            duration = 400
            interpolator = OvershootInterpolator(3f) // 设置回弹插值器
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.RED

        // 两圆之间的距离
        val d = distance()
        var ratio = d / MAX_RADIUS
        Log.e("szj两个圆之间的距离", "距离为:$d\t${ratio}")
        if (ratio > 0.618) {
            ratio = 0.618f
        }
        // 小圆半径
        val smallRadius = SMALL_RADIUS - SMALL_RADIUS * ratio
        // 绘制小圆
        canvas.drawCircle(smallPointF.x, smallPointF.y, smallRadius, paint)

        // 大圆位置是否在辅助圆内
        if (bigPointF.contains(smallPointF, MAX_RADIUS)) {
            // 绘制大圆
            canvas.drawCircle(bigPointF.x, bigPointF.y, BIG_RADIUS, paint)

            // 绘制贝塞尔
            drawBezier(canvas, smallRadius, BIG_RADIUS)
        }

        // 绘制爆炸效果
        if (explodeIndex != -1) {
            // 圆和bitmap坐标系不同
            // 圆的坐标系是中心点
            // bitmap的坐标系是左上角
            canvas.drawBitmap(explodeImages[explodeIndex],
                bigPointF.x - bitMapWidth / 2,
                bigPointF.y - bitMapWidth / 2,
                paint)
        }
        // 绘制ViewBitMap图片
        bitMap?.let {
            Log.e("szj绘制BitMap",
                "width:${bigPointF.x - it.width / 2f}\theight:${bigPointF.y - it.height / 2f}")
            canvas.drawBitmap(it,
                bigPointF.x - it.width / 2f,
                bigPointF.y - it.height / 2f, paint)
        }
        // 绘制辅助圆
        paint.color = Color.argb(20, 255, 0, 0)
        canvas.drawCircle(smallPointF.x, smallPointF.y, MAX_RADIUS, paint)


    }

    var bitMap: Bitmap? = null
    var bitMapWidth = 0f
    fun upDataBitMap(bitMap: Bitmap?, bitMapWidth: Float) {
        this.bitMap = bitMap
        this.bitMapWidth = bitMapWidth
        invalidate()
    }

    // 大圆是否在辅助圆内
    fun isContains() = let {
        bigPointF.contains(smallPointF, MAX_RADIUS)
    }

    /*
   * 作者:史大拿
   * 创建时间: 8/17/22 3:11 PM
   * TODO 绘制贝塞尔曲线
   * @param smallRadius: 小圆半径
   * @param bigRadius: 大圆半径
   */
    private fun drawBezier(canvas: Canvas, smallRadius: Float, bigRadius: Float) {
        val current = bigPointF - smallPointF

        val BF = current.y.toDouble()
        val FD = current.x.toDouble()
        //
        val BDF = atan(BF / FD)

        val p1X = smallPointF.x + smallRadius * sin(BDF)
        val p1Y = smallPointF.y - smallRadius * cos(BDF)

        val p2X = bigPointF.x + bigRadius * sin(BDF)
        val p2Y = bigPointF.y - bigRadius * cos(BDF)

        val p3X = smallPointF.x - smallRadius * sin(BDF)
        val p3Y = smallPointF.y + smallRadius * cos(BDF)

        val p4X = bigPointF.x - bigRadius * sin(BDF)
        val p4Y = bigPointF.y + bigRadius * cos(BDF)

        // 控制点
        val controlPointX = current.x / 2 + smallPointF.x
        val controlPointY = current.y / 2 + smallPointF.y

        val path = Path()
        path.moveTo(p1X.toFloat(), p1Y.toFloat()) // 移动到p1位置
        path.quadTo(controlPointX, controlPointY, p2X.toFloat(), p2Y.toFloat()) // 绘制贝塞尔

        path.lineTo(p4X.toFloat(), p4Y.toFloat()) // 连接到p4
        path.quadTo(controlPointX, controlPointY, p3X.toFloat(), p3Y.toFloat()) // 绘制贝塞尔
        path.close() // 连接到p1
        canvas.drawPath(path, paint)
    }


    // 小圆与大圆之间的距离
    private fun distance(): Float {
        val current = bigPointF - smallPointF
        return sqrt(current.x.toDouble().pow(2.0) + (current.y.toDouble().pow(2.0))).toFloat()
    }


}