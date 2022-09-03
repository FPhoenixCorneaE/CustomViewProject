package com.example.customviewproject.c.view.c5

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import com.example.customviewproject.R
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 *
 * @ClassName: RedEnvelopeView
 * @Author: 史大拿
 * @CreateDate: 9/3/22$ 11:28 AM$
 * TODO
 */
class RedEnvelopeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 红包bitmap
    private val honBaoBitMap = getBitMap(R.mipmap.hongbao)

    // 总进度
    var totalProgress = 5

    // 当前进度
    var currentProgress = 1
        set(value) {
            field = value
            invalidate()
        }

    // 当前位置
    var currentPosition = 0f
        set(value) {
            field = value
            invalidate()
        }

    // 是否每一格都扩散
    var isDiffusion = true

    // 扩散范围
    var diffusionRadius = 0f
        set(value) {
            field = value
            invalidate()
        }

    // 扩散最大范围
    private val diffusionRadiusMax by lazy { max(width, height) * 0.2f }

    // 扩散图片个数
    private val diffusionNumber = 8

    // 扩散的图片
    val diffusionBitmaps by lazy {
        arrayListOf(
            getBitMap(R.mipmap.icon_red_package_bomb_1),
            getBitMap(R.mipmap.icon_red_package_bomb_2)
        )
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 进度条走动动画
    private val processBarAnimator by lazy {
        ObjectAnimator.ofInt(this, "currentProgress", totalProgress).apply {
            duration = 2000
            interpolator = null
        }
    }

    // 扩散动画
    private val diffusionAnimator by lazy {
        ObjectAnimator.ofFloat(this, "diffusionRadius", diffusionRadiusMax).apply {
            duration = 1000
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 因为有扩散效果，所以需要 *1.5
        val width = resolveSize((honBaoBitMap.width * 1.5).toInt(), widthMeasureSpec)
        val height = resolveSize((honBaoBitMap.height * 1.5).toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        processBarAnimator.start()

        processBarAnimator.addUpdateListener {
            Log.e("szj执行了", "23")
        }
    }

    override fun onDraw(canvas: Canvas) {

        paint.alpha = 255

        val left = width / 2 - honBaoBitMap.width / 2f
        val top = height / 2 - honBaoBitMap.height / 2f

        // TODO 绘制红包
        canvas.drawBitmap(
            honBaoBitMap, left,
            top,
            paint
        )

        // TODO 绘制背景
        val progressBackGroundRect = drawProgressBarBackground(canvas, left, top)

        // TODO 绘制当前进度
        val progressRect = drawProgressBar(canvas, progressBackGroundRect)

        // TODO 绘制扩散的图片
        val centerX = progressRect.right
        val centerY = (progressRect.bottom - progressRect.top) / 2 + progressRect.top
        drawBitmap(canvas, centerX, centerY, diffusionRadius)
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/3/22 2:19 PM
     * TODO
     * @param centerX:圆心X位置
     * @param centerY:圆心Y位置
     * @param radius:半径
     */
    private fun drawBitmap(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        var angle = 0f

        if (radius == 0f) return
        // radius 当前进度
        // diffusionRadiusMax 总进度
        // 已经执行的进度

        // 当前分 / 总分 * 255
        paint.alpha = 255 - (((radius / diffusionRadiusMax) * 255)).toInt()


        Log.e(
            "szjpaintAlpha",
            "radius:${radius}\tdiffusionRadiusMax:${diffusionRadiusMax}\talpha:${(paint.alpha)}"
        )

        // 循环每一个图片
        repeat(diffusionNumber) {
            val bitmap = diffusionBitmaps[it % diffusionBitmaps.size]

            val left = radius * sin(Math.toRadians(angle.toDouble())).toFloat() + centerX
            val top = radius * cos(Math.toRadians(angle.toDouble())).toFloat() + centerY

            canvas.drawBitmap(
                bitmap,
                left - (bitmap.width / 2f),
                top - (bitmap.height / 2f),
                paint
            )

            angle += 360 / diffusionNumber
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/3/22 2:08 PM
     * TODO 绘制当前进度
     * @return : 当前进度条位置
     */
    private fun drawProgressBar(canvas: Canvas, rect: RectF) = let {
        // 一格的进度
        val progress = (rect.right - rect.left) / totalProgress

        // 一格的进度 * 当前的进度 + 左侧的距离 = 当前的right
        rect.right = progress * currentProgress + rect.left

        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        // 绘制当前进度
        canvas.drawRoundRect(rect, rect.bottom / 2f, rect.bottom / 2f, paint)
        rect
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/3/22 1:46 PM
     * TODO 绘制进度条背景
     * @return: 进度条范围
     */
    private fun drawProgressBarBackground(canvas: Canvas, left: Float, top: Float) = let {

        paint.color = Color.RED
        paint.strokeWidth = 1.dp
        paint.style = Paint.Style.FILL

        val rect = RectF(
            honBaoBitMap.width * 0.1f + left,
            honBaoBitMap.height * 0.85f + top,
            honBaoBitMap.width * 0.9f + left,
            honBaoBitMap.height * 1f + top
        )
        // 绘制红色背景
        canvas.drawRoundRect(rect, rect.bottom / 2f, rect.bottom / 2f, paint)


        paint.color = Color.YELLOW
        paint.style = Paint.Style.STROKE
        // 绘制黄色边框
        canvas.drawRoundRect(rect, rect.bottom / 2f, rect.bottom / 2f, paint)
        rect
    }
}