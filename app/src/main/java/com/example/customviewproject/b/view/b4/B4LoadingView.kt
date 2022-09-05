package com.example.customviewproject.b.view.b4

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnRepeat
import com.example.customviewproject.R
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap
import kotlin.math.abs
import kotlin.math.sign

/**
 *
 * @ClassName: B4LoadingView
 * @Author: 史大拿
 * @CreateDate: 9/5/22$ 10:16 AM$
 * TODO
 */
class B4LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 图片变换时间
        const val BEAT_TIME = 1000L

        // 向上平移距离
        val TRANSLATE_DISTANCE_MAX = (-100).dp

        const val TEXT = "拼命加载中..."

        // 文字大小
        val TEST_SIZE = 16.dp
    }

    private val bitmaps by lazy {
        val width = width / 8
        arrayListOf(
            getBitMap(R.mipmap.sanjiaoxing, width),
            getBitMap(R.mipmap.shixinzhengfangxing, width),
            getBitMap(R.mipmap.circle, width),
        )
    }

    // 图片下标
    private var bitmapIndex = 0
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 向上平移距离
    private var translateDistance = 0f
        set(value) {
            field = value
            invalidate()
            postInvalidate()
        }

    //旋转角度
    private var rotate = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val rotateAnimator by lazy {
        ObjectAnimator.ofFloat(this, "rotate", 360f).apply {
            repeatMode = ValueAnimator.RESTART
            duration = 1000
            repeatCount = -1
        }
    }

    // 记录原始位置
    private var originTopLocation = 0f


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 初始化原始位置
        originTopLocation = height / 2f - bitmaps[bitmapIndex].height / 2f

        Log.e("szjOriginTop1", (originTopLocation).toString())
        var temp = 0
        moveAnimator.doOnRepeat {
            Log.i("szj翻转中", ".${bitmapIndex}")
            if (temp % 2 == 0) {
                bitmapIndex++
            }
            temp++
        }

        // 旋转
        rotateAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 图片宽高
        val bitmapWidth = bitmaps[bitmapIndex % bitmaps.size].width
        val bitmapHeight = bitmaps[bitmapIndex % bitmaps.size].height

        val left = width / 2f - bitmapWidth / 2f
        val top = originTopLocation

        // TODO 绘制bitmap
        drawBitMap(left, top, canvas)

        // TODO 绘制阴影
        val bottomLocation = drawRoundRect(bitmapWidth, bitmapHeight, left, top, canvas)

        // TODO 绘制文字
        drawText(bottomLocation, canvas)
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/5/22 2:59 PM
     * TODO 绘制文字
     * @param bottomLocation: 阴影底部位置
     */
    private fun drawText(bottomLocation: Float, canvas: Canvas) {
        paint.textSize = TEST_SIZE
        // 测量文字宽度
        val textWidth = paint.measureText(TEXT)
        val dx = width / 2f - textWidth / 2f
        // 当前偏移量
        val offSetY = -paint.fontMetrics.top + bottomLocation + 4.dp

        val length = bitmapIndex % TEXT.length

        // 绘制文字
        canvas.drawText(TEXT, 0, length, dx, offSetY, paint)
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/5/22 2:51 PM
     * TODO 阴影
     * @param bitmapWidth,bitmapHeight:图片宽高
     * @param left,top: 图片左上角位置
     * @return: 阴影底部距离
     * 作用: 用于寻找绘制文字合适位置
     */
    private fun drawRoundRect(
        bitmapWidth: Int,
        bitmapHeight: Int,
        left: Float,
        top: Float,
        canvas: Canvas
    ): Float {

        // 当前距离
        val tempTranslateD = abs(translateDistance)
        // 总距离
        val tempTranslateDax = abs(TRANSLATE_DISTANCE_MAX)

        // 计算比例
        val ratio = tempTranslateD / tempTranslateDax

        // 计算缩放距离
        val temp = (bitmapWidth / 2) * ratio

        val tempTop = top + bitmapHeight + 10.dp

        val tempBottom = tempTop + 2.dp
        // 绘制矩形
        canvas.drawRoundRect(
            left + temp,
            tempTop,
            (bitmapWidth + left) - temp,
            tempBottom,
            10.dp,
            10.dp,
            paint
        )

        return tempBottom

    }

    /*
     * 作者:史大拿
     * 创建时间: 9/5/22 2:51 PM
     * TODO 绘制bitmap
     * @param left,top:图片左上角位置
     */
    private fun drawBitMap(left: Float, top: Float, canvas: Canvas) {

        // Y轴平移
        canvas.translate(0f, translateDistance)
        // 旋转
        canvas.rotate(rotate, width / 2f, height / 2f)
        // 绘制上下滚动图片
        canvas.drawBitmap(bitmaps[bitmapIndex % bitmaps.size], left, top, paint)
        canvas.rotate(-rotate, width / 2f, height / 2f)
        canvas.translate(0f, -translateDistance)
    }


    // 向上移动动画
    private val moveAnimator by lazy {
        ObjectAnimator.ofFloat(this, "translateDistance", TRANSLATE_DISTANCE_MAX).also {
            it.duration = BEAT_TIME
            it.repeatMode = ValueAnimator.REVERSE
            it.repeatCount = -1
            it.interpolator = AccelerateInterpolator()
            it.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        moveAnimator.removeAllListeners()
        rotateAnimator.removeAllListeners()
    }

}