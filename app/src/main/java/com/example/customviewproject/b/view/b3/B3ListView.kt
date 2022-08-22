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
 * @ClassName: B3ListView
 * @Author: 史大拿
 * @CreateDate: 8/22/22$ 2:24 PM$
 * TODO
 */
class B3ListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 随机个数
        const val RANDOM_NUMBER = 10
    }

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


    var pointF = arrayListOf<PointF>()
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


        val listP0 = arrayListOf<PointF>()
        val listP1 = arrayListOf<PointF>()
        val listP2 = arrayListOf<PointF>()
        val listP3 = arrayListOf<PointF>()

        repeat(RANDOM_NUMBER) {
            listP0.add(PointF(width / 2f - bitmapWidth / 2, height - bitmapHeight)) // 开始位置

            listP1.add(PointF((0 until width).random().toFloat(),
                (0 until height / 2).random().toFloat())) // 控制点1


            listP2.add(PointF((0 until width).random().toFloat(),
                height / 2f + (0 until height / 2).random().toFloat())) // 控制点2


            val endRandom = (0 until width).random()
            // 如果当前随机数小于图片宽度 说明在最左侧并且超出了屏幕
            val endX = if (endRandom < bitmapWidth) {
                bitmapWidth // 让他不超出屏幕
            } else if (endRandom + bitmapWidth > width) {
                // 如果当前随机数+ 图片宽度 > view宽度 说明现在右侧超出屏幕了
                endRandom - bitmapHeight
            } else {
                endRandom //
            }
            listP3.add(PointF(endX.toFloat(), 0f)) // 结束点
        }

        Log.e("szjWidthRandom", "${(0 until width).random()}")
        val animator = ObjectAnimator.ofObject(this,
            "pointF",
            C3TypeEvaluatorList(listP1, listP2),
            listP0,
            listP3)
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
        repeat(RANDOM_NUMBER) {
            pointF.add(PointF())
        }
        repeat(RANDOM_NUMBER) {
            pointF[it].x = width / 2f - bitmapWidth / 2
            pointF[it].y = height - bitmapHeight
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e("szjAnimatedValue", "${paint.alpha}")

        repeat(RANDOM_NUMBER) {
            canvas.drawBitmap(bitmaps[it],
                pointF[it].x,
                pointF[it].y,
                paint)
        }

    }

    inner class C3TypeEvaluatorList(private val p1: List<PointF>, private val p2: List<PointF>) :
        TypeEvaluator<List<PointF>> {

        override fun evaluate(t: Float, p0: List<PointF>, p3: List<PointF>): List<PointF> {
            if (!(p0.size == p1.size && p2.size == p3.size && p0.size == p2.size)) {
                throw RuntimeException("长度不匹配")
            }
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
            val list = arrayListOf<PointF>()
            repeat(RANDOM_NUMBER) {
                val currentP = PointF(
                    p0[it].x * (1 - t) * (1 - t) * (1 - t)
                            + 3 * p1[it].x * t * (1 - t) * (1 - t)
                            + 3 * p2[it].x * t * t * (1 - t)
                            + p3[it].x * t * t * t,
                    p0[it].y * (1 - t) * (1 - t) * (1 - t)
                            + 3 * p1[it].y * t * (1 - t) * (1 - t)
                            + 3 * p2[it].y * t * t * (1 - t)
                            + p3[it].y * t * t * t
                )
                list.add(currentP)
            }

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

            return list
        }
    }


}