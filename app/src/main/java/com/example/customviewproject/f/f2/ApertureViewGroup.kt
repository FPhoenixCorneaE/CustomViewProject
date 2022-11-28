package com.example.customviewproject.f.f2

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.core.graphics.withSave
import androidx.core.view.setPadding
import com.example.customviewproject.R
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: apertureView
 * @Author: 史大拿
 * @CreateDate: 11/28/22$ 1:35 PM$
 * TODO
 */
class ApertureViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mColor1 = 0
    private var mColor2 = 0

    // 边框宽度
    private var mBorderWidth = 0f

    // 边框角度
    private var mBorderAngle = 0f

    // 光圈速度
    private var mDuration = 0

    // 中间颜色
    private var mMiddleColor = 0

    init {
//         设置view圆角
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                // 设置圆角率为
                outline.setRoundRect(0, 0, view.width, view.height, mBorderAngle)
            }
        }
        clipToOutline = true

        val a = context.obtainStyledAttributes(attrs, R.styleable.ApertureViewGroup)

        try {
            mColor1 = a.getColor(R.styleable.ApertureViewGroup_aperture_color1, Color.YELLOW)
            mColor2 = a.getColor(R.styleable.ApertureViewGroup_aperture_color2, -1)
            mBorderWidth =
                a.getInt(R.styleable.ApertureViewGroup_aperture_border_width, 20.dp.toInt())
                    .toFloat()
            setPadding(mBorderWidth.toInt() / 2)
//            setPadding(mBorderWidth.toInt(), 0, mBorderWidth.toInt(), 0)
            mBorderAngle =
                a.getInt(R.styleable.ApertureViewGroup_aperture_border_angle, 20.dp.toInt())
                    .toFloat()

            mDuration = a.getInt(R.styleable.ApertureViewGroup_aperture_duration, 3000)

            mMiddleColor =
                a.getColor(R.styleable.ApertureViewGroup_aperture_middle_color, Color.BLACK)
        } finally {
            a.recycle()
        }
    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val rectF by lazy {
        val left = 0f + mBorderWidth / 2f
        val top = 0f + mBorderWidth / 2f
        val right = left + width - mBorderWidth
        val bottom = top + height - mBorderWidth
        RectF(left, top, right, bottom)
    }

    private val color1 by lazy {
        LinearGradient(
            width * 1f,
            height / 2f,
            width * 1f,
            height * 1f,
            intArrayOf(Color.TRANSPARENT, mColor1),
            floatArrayOf(0f, 0.9f),
//            intArrayOf(mColor1, mColor1),
//            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )
    }


    private val color2 by lazy {
        LinearGradient(
            width / 2f,
            height / 2f,
            width / 2f,
            0f,
            intArrayOf(Color.TRANSPARENT, mColor2),
            floatArrayOf(0f, 0.9f),
//            intArrayOf(mColor1, mColor1),
//            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )
    }
    private val animator by lazy {
        val animator = ObjectAnimator.ofFloat(this, "currentSpeed", 0f, 360f)
        animator.repeatCount = -1
        animator.repeatMode = ObjectAnimator.RESTART
        animator.interpolator = null
        animator.duration = mDuration.toLong()
        animator
    }

    private var currentSpeed = 0f
        set(value) {
            field = value
            invalidate()
        }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        animator.start()
    }


    override fun dispatchDraw(canvas: Canvas) {
        val left1 = width / 2f
        val top1 = height / 2f

        val right1 = left1 + width
        val bottom1 = top1 + width
        canvas.withSave {
            canvas.rotate(currentSpeed, width / 2f, height / 2f)
//            Log.i("draw", "rectF:${rectF}\twidth:${width}\theight:${height}")
            // 绘制渐变view1
            paint.shader = color1
            canvas.drawRect(left1, top1, right1, bottom1, paint)
            paint.shader = null

            if (mColor2 != -1) {
                // 绘制渐变view2
                paint.shader = color2
                canvas.drawRect(left1, top1, -right1, -bottom1, paint)
                paint.shader = null
            }
        }

        paint.color = mMiddleColor
        canvas.drawRoundRect(rectF, mBorderAngle, mBorderAngle, paint)

        super.dispatchDraw(canvas)

    }
}