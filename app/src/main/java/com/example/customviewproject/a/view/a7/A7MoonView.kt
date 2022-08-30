package com.example.customviewproject.a.view.a7

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: A7MoonView
 * @Author: 史大拿
 * @CreateDate: 8/29/22$ 1:59 PM$
 * TODO 参考自: https://juejin.cn/post/7009299995954249759
 */
class A7MoonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr){

    private var lightAlpha = 155
        set(value) {
            field = value
            invalidate()
        }
    private val paint = Paint().apply {
//        color = Color.WHITE
        strokeWidth = 2.dp
//        style = Paint.Style.STROKE

        // @param radius: 光晕范围
        maskFilter = BlurMaskFilter(1000f, BlurMaskFilter.Blur.OUTER)
    }

    private val lightAlphaAnimation by lazy {
        ObjectAnimator.ofInt(this, "lightAlpha", 155, 255).apply {
            duration = 1050
            repeatMode = ValueAnimator.REVERSE // 反向执行
            repeatCount = -1  // 重复执行
            interpolator = LinearInterpolator()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lightAlphaAnimation.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        paint.color = Color.argb(lightAlpha, 255, 255, 0)

        canvas.drawCircle(width / 2f, height / 2f, width / 4f, paint)
    }

}