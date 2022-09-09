package com.example.customviewproject.c.view.c2

import android.animation.ObjectAnimator
import android.animation.PointFEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.graphics.minus
import com.example.customviewproject.R
import com.example.customviewproject.ext.contains
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap
import kotlin.math.*

/**
 *
 * @ClassName: C1View
 * @Author: 史大拿
 * @CreateDate: 8/17/22$ 10:35 AM$
 * TODO
 */
class C2View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    var bitMapWidth = 0f

    // 加载20张图片 对应 [explode_0.jpg .. explode_19.jpg]
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
    private var explodeIndex = -1
        set(value) {
            field = value
            invalidate()
        }

    companion object {
        // 大圆半径
        private val BIG_RADIUS = 20.dp

        // 小圆半径
        private val SMALL_RADIUS = BIG_RADIUS

        // 最大范围(半径)，超出这个范围大圆不显示
        private val MAX_RADIUS = 150.dp
    }

    // 大圆位置 tips:这里不能private,因为属性动画要用
    var bigPointF = PointF(0f, 0f)
        set(value) {
            field = value
            invalidate()
        }

    // 小圆位置
    private var smallPointF = PointF(0f, 0f)

    // 原始位置(DOWN事件时候的位置) (初始化时候的位置)
//    private val originPointF = PointF(0f, 0f)

    // 爆炸效果动画
    val explodeAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofInt(this, "explodeIndex", 19, -1).apply {
            duration = 1000
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize((MAX_RADIUS * 2).toInt(), widthMeasureSpec)
        val height = resolveSize((MAX_RADIUS * 2).toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    // viewBitMap
    var viewBitMap: Bitmap? = null
        set(value) {
            field = value
            // 这里其实不用刷新，因为在MOVE的时候一直在刷新
//            invalidate()
        }


    // 初始化起始点 (小圆位置)
    fun initPointF(x1: Float, y1: Float, x2: Float, y2: Float) {
        // 小圆位置
        smallPointF.x = x1
        smallPointF.y = y1

        // 大圆位置
        bigPointF.x = x2
        bigPointF.y = y2

//        originPointF.x = x1
//        originPointF.y = y1
        invalidate()
    }

    // 更新移动点 (大圆位置)
    fun upDataPointF(x: Float, y: Float) {
        bigPointF.x = x
        bigPointF.y = y
        invalidate()
    }


//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        bigPointF = PointF(width / 2f, height / 2f)
//        smallPointF = PointF(width / 2f, height / 2f)
//    }

//    var isMove = false

//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                isMove = bigPointF.contains(PointF(event.x, event.y), BIG_RADIUS)
//
//            }
//            MotionEvent.ACTION_MOVE -> {
//                if (isMove) {
//                    // 如果当前点击的位置 包含 bigPointF 说明选中了
//                    bigPointF.x = event.x
//                    bigPointF.y = event.y
//                } else {
//                    bigPointF.x = width / 2f
//                    bigPointF.y = height / 2f
//                }
//
//            }
//            MotionEvent.ACTION_UP -> {
//                // 如果大圆位置超出拖拽范围，不回弹
//                if (bigPointF.contains(smallPointF, MAX_RADIUS)) {
//                    bigAnimator().start()
//                } else {
//                    // 绘制爆炸效果
//                    explodeAnimator.start()
//                    // 爆炸效果结束后，将图片移动到原始位置
//                    explodeAnimator.doOnEnd {
//                        bigPointF.x = width / 2f
//                        bigPointF.y = height / 2f
//                    }
//                }
//            }
//        }
//
//        invalidate()
//        return true
//    }

    fun bigAnimator(): ValueAnimator {
        return ObjectAnimator.ofObject(this, "bigPointF", PointFEvaluator(),
            PointF(smallPointF.x, smallPointF.y)).apply {
            duration = 400
            interpolator = OvershootInterpolator(3f) // 设置回弹插值器
        }
    }

    // 大圆是否在小圆内
    fun isContains() = let {
        bigPointF.contains(smallPointF, MAX_RADIUS)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        paint.color = Color.RED

        // 两圆之间的距离
        val d = distance()
        var ratio = d / MAX_RADIUS
        if (ratio > 0.6) {
            ratio = 0.6f
        }
        Log.e("szj两个圆之间的距离", "距离为:$d\t${ratio}")

        // 小圆半径
        val smallRadius = SMALL_RADIUS - SMALL_RADIUS * ratio
        // 小圆
        canvas.drawCircle(smallPointF.x,
            smallPointF.y,
            smallRadius,
            paint)

        val isContains = bigPointF.contains(smallPointF, MAX_RADIUS)
        Log.e("szj是否包含",
            "isContains:$isContains\tbigPointF:$bigPointF\tsmallPointF:$smallPointF")
        if (isContains) {
            // 绘制大圆
            canvas.drawCircle(bigPointF.x, bigPointF.y, BIG_RADIUS, paint)

            // 绘制贝塞尔
            drawBezier(canvas, smallRadius, BIG_RADIUS)
        }
        // 绘制BitMap
        viewBitMap?.let {
            canvas.drawBitmap(it,
                bigPointF.x - it.width / 2f,
                bigPointF.y - it.height / 2f, paint)
        }

        // 绘制爆炸效果
        if (explodeIndex != -1) {
            canvas.drawBitmap(explodeImages[explodeIndex],
                bigPointF.x - bitMapWidth / 2,
                bigPointF.y - bitMapWidth / 2,
                paint)
        }


        // 辅助圆范围
//        paint.color = Color.argb(30, 255, 0, 0)
//        canvas.drawCircle(smallPointF.x, smallPointF.y, MAX_RADIUS, paint)

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
        // BDF = BAD
        val BDF = atan(BF / FD)

        val p1X = smallPointF.x + smallRadius * sin(BDF)
        val p1Y = smallPointF.y - smallRadius * cos(BDF)

        val p2X = bigPointF.x + bigRadius * sin(BDF)
        val p2Y = bigPointF.y - bigRadius * cos(BDF)

        // 控制点
        val controlPointX = current.x / 2 + smallPointF.x
        val controlPointY = current.y / 2 + smallPointF.y

        val p3X = smallPointF.x - smallRadius * sin(BDF)
        val p3Y = smallPointF.y + smallRadius * cos(BDF)

        val p4X = bigPointF.x - bigRadius * sin(BDF)
        val p4Y = bigPointF.y + bigRadius * cos(BDF)

        val path = Path()
        path.moveTo(p1X.toFloat(), p1Y.toFloat())
        path.quadTo(controlPointX, controlPointY, p2X.toFloat(), p2Y.toFloat())

        path.lineTo(p4X.toFloat(), p4Y.toFloat())
        path.quadTo(controlPointX, controlPointY, p3X.toFloat(), p3Y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }

    // 小圆与大圆之间的距离
    private fun distance(): Float {
        val current = bigPointF - smallPointF
        return sqrt(current.x.toDouble().pow(2.0) + (current.y.toDouble().pow(2.0))).toFloat()
    }


    // 更新图片
    fun upDataBitMap(bitMap: Bitmap?, width: Float) {
        viewBitMap = bitMap
        bitMapWidth = width
        invalidate()
    }

}

