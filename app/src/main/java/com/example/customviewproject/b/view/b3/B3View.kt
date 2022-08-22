package com.example.customviewproject.b.view.b3

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.R
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: C3View
 * @Author: 史大拿
 * @CreateDate: 8/22/22$ 2:24 PM$
 * TODO
 */
class B3View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val bitmaps = run {
        val list = arrayListOf<Bitmap>()
        val bitmapWidth = 40.dp
        list.add(getBitMap(R.mipmap.emoji_0, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_1, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_2, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_3, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_4, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_5, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_6, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_7, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_8, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_9, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_10, bitmapWidth.toInt()))
        list.add(getBitMap(R.mipmap.emoji_11, bitmapWidth.toInt()))
        list
    }

    private val paint = Paint().apply {
        strokeWidth = 4.dp
        color = Color.RED
    }


    var pointF = PointF()
        set(value) {
            field = value
            Log.e("szjPointF", "$field")

            invalidate()
        }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                bitmapAnimator()
            }
        }
        return true
    }

    private fun bitmapAnimator() {
        val bitmapWidth = bitmaps[0].width.toFloat()
        val bitmapHeight = bitmaps[0].height.toFloat()
        val p0 = PointF(width / 2f - bitmapWidth / 2, height - bitmapHeight) // 开始点

        val p1 = PointF((0 until width).random().toFloat(),
            (0 until height / 2).random().toFloat()) // 控制点1

        val p2 = PointF((0 until width).random().toFloat(),
            height / 2f + (0 until height / 2).random().toFloat()) // 控制点2

        val p3 = PointF((0 until width).random().toFloat(), 0f) // 结束点

        Log.e("szjWidthRandom", "${(0 until width).random()}")
        val animator = ObjectAnimator.ofObject(this,
            "pointF",
            C3TypeEvaluator(p1, p2),
            p0,
            p3)
        animator.duration = 3000
        animator.start()
        animator.addUpdateListener {
            // 设置画笔透明度
            paint.alpha = (255 * (1 - it.animatedFraction) + 255 * 0.2).toInt()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val bitmapWidth = bitmaps[0].width.toFloat()
        val bitmapHeight = bitmaps[0].height.toFloat()
        pointF.x = width / 2f - bitmapWidth / 2
        pointF.y = height - bitmapHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bitmap = bitmaps[0]
        Log.e("szjAnimatedValue", "${paint.alpha}")


        canvas.drawBitmap(bitmap,
            pointF.x,
            pointF.y,
            paint)
    }

    inner class C3TypeEvaluator(private val p1: PointF, private val p2: PointF) :
        TypeEvaluator<PointF> {

        override fun evaluate(t: Float, p0: PointF, p3: PointF): PointF {
            // 三阶贝塞尔公式地址: https://baike.baidu.com/item/贝塞尔曲线/1091769

//            pointF.x = p0.x * ((1 - t).pow(3))
//            +3 * p1.x * t * ((1 - t).pow(2))
//            +3 * p2.x * t.pow(2) * (1 - t)
//            +p3.x * t.pow(3)
//
//            pointF.y = p0.y * ((1 - t).pow(3))
//            +3 * p1.y * t * ((1 - t).pow(2))
//            +3 * p2.y * t.pow(2) * (1 - t)
//            +p3.y * t.pow(3)
            val currentP = PointF(
                p0.x * (1 - t) * (1 - t) * (1 - t)
                        + 3 * p1.x * t * (1 - t) * (1 - t)
                        + 3 * p2.x * t * t * (1 - t)
                        + p3.x * t * t * t,
                p0.y * (1 - t) * (1 - t) * (1 - t)
                        + 3 * p1.y * t * (1 - t) * (1 - t)
                        + 3 * p2.y * t * t * (1 - t)
                        + p3.y * t * t * t
            )
//            currentP.x =
//                p0.x * (1 - t) * (1 - t) * (1 - t)
//            +3 * p1.x * t * (1 - t) * (1 - t)
//            +3 * p2.x * t * t * (1 - t)
//            +p3.x * t * t * t
//
//            currentP.y = p0.y * (1 - t) * (1 - t) * (1 - t)
//            +3 * p1.y * t * (1 - t) * (1 - t)
//            +3 * p2.y * t * t * (1 - t)
//            +p3.y * t * t * t

            Log.e("szjCurrentP", "$currentP")
            return currentP
        }
    }
}