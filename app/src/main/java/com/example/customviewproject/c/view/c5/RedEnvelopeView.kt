package com.example.customviewproject.c.view.c5

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateInterpolator
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
open class RedEnvelopeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 红包bitmap
    private val honBaoBitMap = getBitMap(R.mipmap.hongbao)

    // 总进度
    private var totalProgress = 3f

    // 当前进度
    private var currentProgress = 0f
        set(value) {
            field = value
            invalidate()
        }

    // 扩散范围
    private var diffusionRadius = 0f
        set(value) {
            field = value
            invalidate()
        }

    // 扩散最大范围
    private var diffusionRadiusMax = 0f

    // 扩散图片个数
    private val diffusionNumber = 8

    // 扩散的图片
    private val diffusionBitmaps by lazy {
        arrayListOf(
            getBitMap(R.mipmap.icon_red_package_bomb_1),
            getBitMap(R.mipmap.icon_red_package_bomb_2)
        )
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 原始范围
    private var originRadiusMax = 0f

    // 进度条走动动画
    val processBarAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "currentProgress", totalProgress).apply {
            duration = 2000
            interpolator = null
        }
    }


    companion object {
        // 缩放比例
        private const val SCALE_MAX = 1.2f
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
        originRadiusMax = max(width, height) * 0.2f
        diffusionRadiusMax = originRadiusMax
        processBarAnimator.start()

        // 进度条动画
        processBarAnimator.doOnEnd {
            // 扩散动画
            val animator = diffusionAnimator()
            animator.doOnEnd {
                // 放大动画
                executeMagnifyAnimator()
            }
            animator.start()
        }
    }

    // 扩散动画
    private fun diffusionAnimator(duration: Long = 1000L) = let {
        ObjectAnimator.ofFloat(this, "diffusionRadius", diffusionRadiusMax).also {
            it.duration = duration
        }
    }


    // 是否执行放大动画
    private var isZoom = false

    // 是否是放大状态
    private var isScaleBig = false


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

        // 如果已经满了，绘制扩散图片
        if (currentProgress == totalProgress) {
            val centerX: Float
            val centerY: Float
            if (!isZoom) {
                // 没有执行放大动画
                // TODO 绘制扩散的图片
                centerX = progressRect.right
                centerY = (progressRect.bottom - progressRect.top) / 2 + progressRect.top
            } else {
                centerX = width / 2f
                centerY = height * 0.4f
            }

            drawBitmap(canvas, centerX, centerY, diffusionRadius)
        }
    }


    // 放大动画
    private fun executeMagnifyAnimator() = let {
        val set = AnimatorSet()

        val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, SCALE_MAX)
        val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, SCALE_MAX)

        // 两个动画同时执行
        set.play(scaleXAnimator).with(scaleYAnimator)
        set.interpolator = AnticipateInterpolator(3f)
        set.duration = 1000
        set.start()
        set.doOnEnd {
            // 放大动画结束后 继续"撒花"
            // 缩小
            isZoom = true

            // 默认半径
            diffusionRadius = 0f

            diffusionRadiusMax = max(width, height) * 0.5f
            diffusionAnimator().also {
                it.start()
                it.doOnEnd {
                    executeShrinkAnimator()
                }
            }
            isScaleBig = true
        }
        set
    }


    // 缩小动画
    private fun executeShrinkAnimator() {
        val set = AnimatorSet()
        val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", SCALE_MAX, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", SCALE_MAX, 1f)

        // 两个动画同时执行
        set.play(scaleXAnimator).with(scaleYAnimator)
        set.duration = 1000
        set.start()
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/5/22 9:54 AM
     * TODO 重置
     */
    open fun reset() {
        currentProgress = 0f
        isZoom = false // 是否缩放
        isScaleBig = false // 是否是放大状态
        diffusionRadius = 0f
        diffusionRadiusMax = originRadiusMax
        invalidate()
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

//        Log.e(
//            "szjPaintAlpha",
//            "radius:${radius}\tDiffusionRadiusMax:${diffusionRadiusMax}\tAlpha:${(paint.alpha)}"
//        )

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

//        val currentWidth = rect.width()
//        rect.right = currentWidth * currentProgress / totalProgress + rect.left

        // 一格的进度
        val progress = (rect.right - rect.left) / totalProgress
//         一格的进度 * 当前的进度 + 左侧的距离 = 当前的right
        rect.right = progress * currentProgress + rect.left

        paint.style = Paint.Style.FILL

        // 设置渐变颜色
        paint.shader = LinearGradient(
            rect.left,
            0f,
            rect.right,
            0f,
            Color.RED,
            Color.YELLOW,
            Shader.TileMode.CLAMP
        )
//        paint.color = Color.BLACK
        // 绘制当前进度
        canvas.drawRoundRect(rect, rect.bottom / 2f, rect.bottom / 2f, paint)
        paint.shader = null
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        processBarAnimator.removeAllUpdateListeners()
    }
}