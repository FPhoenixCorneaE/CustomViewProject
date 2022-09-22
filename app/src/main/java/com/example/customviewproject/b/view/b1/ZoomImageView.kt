package com.example.customviewproject.b.view.b1

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import com.example.customviewproject.ext.getBitMap
import com.example.customviewproject.ext.toast
import kotlin.math.max
import kotlin.math.min

/**
 *
 * @ClassName: ZoomImageView
 * @Author: 史大拿
 * @CreateDate: 8/11/22$ 2:41 PM$
 * TODO 缩放ImageView
 * 博客详细教程地址: https://blog.csdn.net/weixin_44819566/article/details/120781647
 */
class ZoomImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener, Runnable, ScaleGestureDetector.OnScaleGestureListener {
//    init {
//        for (i in 0 until attrs!!.attributeCount) {
//            val name = attrs.getAttributeName(i)
//            val value = attrs.getAttributeValue(i)
//            Log.e("szjAttrs", "name:${name}\tvalue:${value}")
//        }
//    }

    private val bitmap = getBitMap()
    private val paint = Paint()

    // 原始偏移
    private var originOffset = PointF(0f, 0f)

    companion object {
        // 最大比率
        private const val BIG_RATIO = 1.5f
    }

    // 大缩放
    private var bigZoom = 0f
    private var smallZoom = 0f

    // 当前放大比率
    private var currentZoom = 0f
        set(value) {
            field = value
            invalidate() // 更新放大比率时刷新
        }

    // 手势监听 (单击,双击,fling,长按等)
    private val gestureDetector = GestureDetectorCompat(context, this).apply {
        setOnDoubleTapListener(this@ZoomImageView) // 双击监听
    }

    // 手势监听 (捏合缩放)
    private val scaleGesture = ScaleGestureDetector(context, this)

    // 记录是否双击
    private var isFlag = false

    // 属性动画 这里切记要延时加载
    private val animator by lazy { ObjectAnimator.ofFloat(this, "currentZoom", smallZoom, bigZoom) }

    // 计算手指移动的偏移量
    private var offset = PointF(0f, 0f)

    // 用来设置fling
    private val overScroll = OverScroller(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originOffset.x = w / 2f - bitmap.width / 2f
        originOffset.y = h / 2f - bitmap.height / 2f

        // view的比率
        val viewRatio = w / h.toFloat()
        // 图片的比率
        val bitMapRatio = bitmap.width / bitmap.height.toFloat()
        // 如果view的比率 > 图片的比率
        if (viewRatio > bitMapRatio) {
            // 放大
            bigZoom = w / bitmap.width.toFloat() * BIG_RATIO // * BIG_RATIO 是为了让图片更大
            smallZoom = h / bitmap.height.toFloat()
        } else {
            bigZoom = h / bitmap.height.toFloat() * BIG_RATIO
            smallZoom = w / bitmap.width.toFloat()
        }
        currentZoom = smallZoom
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val a = (currentZoom - smallZoom) / (bigZoom - smallZoom)
        // 偏移
        canvas.translate(offset.x * a, offset.y * a)

        // 图片以中心点放大
        canvas.scale(currentZoom, currentZoom, width / 2f, height / 2f)
        // 图片居中
        canvas.drawBitmap(bitmap, originOffset.x, originOffset.y, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        scaleGesture.onTouchEvent(event)
        // 是否在进行缩放
        if (!scaleGesture.isInProgress) {
            return gestureDetector.onTouchEvent(event)
        }
        return true
    }

    // down事件
    override fun onDown(e: MotionEvent?) = true

    override fun onShowPress(e: MotionEvent?) {
        Log.i("szjZOOM-onShowPress", e.toString())
    }

    // 点击抬起 如果当前是长按事件,没有抬起操作
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.i("szjZOOM-onSingleTapUp", e.toString())
        return true
    }

    // move事件
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float,
    ): Boolean {
        Log.i("szjZOOM-onScroll", "distanceX:$distanceX\tdistanceY:$distanceY")

        // 如果是放大状态下可以让移动
        if (isFlag) {
            offset.x -= distanceX
            offset.y -= distanceY

            repair()
        }
        return true
    }

    // 修复 为了防止图片越界 (超出图片范围)
    private fun repair() {
        // 左侧边界 手指从左到右滑动
        val left = (bitmap.width * currentZoom / 2 - width / 2)
        offset.x = min(offset.x, left)

        // 右侧边界  手指从右到左滑动
        val right = -(bitmap.width * currentZoom / 2 - width / 2)
        offset.x = max(offset.x, right)

        val top = (bitmap.height * currentZoom / 2 - height / 2)
        offset.y = min(offset.y, top)

        val bottom = -(bitmap.height * currentZoom / 2 - height / 2)
        offset.y = max(offset.y, bottom)

        invalidate()
    }

    override fun onLongPress(e: MotionEvent?) {
        context toast "长按了"

        // 长按事件
        Log.i("szjZOOM-onLongPress", e.toString())
    }

    // onFling 事件
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float,
    ): Boolean {
        Log.i("szjZOOM-onFling", "velocityX:$velocityX\tvelocityY:$velocityX")

        val startX = offset.x.toInt()
        val startY = offset.y.toInt()
        Log.e("szjOffset","offset:$offset\te1:{x:${e1.x},y:${e1.y}}\te2:{x:${e2.x},y:${e2.y}}")
        val vX = velocityX.toInt() // 滑动速度
        val vY = velocityY.toInt()
        val minX = (width / 2 - (bitmap.width * currentZoom) / 2).toInt()
        val maxX = -(width / 2 - (bitmap.width * currentZoom) / 2).toInt()

        val minY = (height / 2 - (bitmap.height * currentZoom) / 2).toInt()
        val maxY = -(height / 2 - (bitmap.height * currentZoom) / 2).toInt()

        overScroll.fling(startX, startY, vX, vY, minX, maxX, minY, maxY, 300, 300)

        // 刷新 绘制行run方法
        postOnAnimation(this)
        return true
    }

    override fun run() {
        // 计算偏移量 返回是否完成
        val isOver = overScroll.computeScrollOffset()
        if (isOver) {
            offset.x = overScroll.currX.toFloat()
            offset.y = overScroll.currY.toFloat()
            Log.e("szjRun","offset:$offset")

            invalidate()
            postOnAnimation(this) //
        }
    }


    // 双击事件中的第一下单击事件
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.i("szjZOOM-TapConfirmed", e.toString())
        return true
    }

    // 双击了
    override fun onDoubleTap(e: MotionEvent): Boolean {
        isFlag = !isFlag
        if (isFlag) { // 图片变大了
            currentZoom = bigZoom

            // 中心点距离当前点击的x位置
            val currentX = e.x - width / 2
            val currentY = e.y - height / 2

            // 放大后的位置
            offset.x = currentX - currentX * bigZoom
            offset.y = currentY - currentY * bigZoom

            // 修复 防止图片出现白边
            repair()

            // 开启动画
            animator.start()

        } else { // 图片变小了
            currentZoom = smallZoom

            animator.reverse()
        }
        invalidate()
        Log.i("szjZOOM-onDoubleTap", e.toString())
        return true
    }

    // 双击过程中的所有事件 DOWN MOVE UP
    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.i("szjZOOM-DoubleTapEvent", e.toString())
        return true
    }

    // 缩放中
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        Log.i("szj捏合onScale",
            "${detector.scaleFactor}\tfocusX:${detector.focusX}\tfocusY:${detector.focusY}")
        // 中心点距离当前点击的x位置
        val currentX = detector.focusX - width / 2
        val currentY = detector.focusY - height / 2
        // 放大后的位置
        offset.x = currentX - currentX * bigZoom
        offset.y = currentY - currentY * bigZoom

        currentZoom *= detector.scaleFactor
        invalidate()
        return true
    }

    // 缩放开始前
    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        Log.i("szj捏合onScaleBegin", "${detector.scaleFactor}")

        return true
    }

    // 缩放结束
    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        // 当前图片的宽
        val currentWidth = bitmap.width * currentZoom
        // 放大后的图片宽
        val smallWidth = bitmap.width * smallZoom
        // 放大后的图片噶
        val bigWidth = bitmap.width * bigZoom

        // 当前宽度 > 最大图片宽度
        if (currentWidth > smallWidth) {
            scaleAnimator(currentZoom, bigZoom)
        } else if (currentWidth < bigWidth) {
            // 当前宽度 < 最小宽度
            scaleAnimator(currentZoom, smallZoom)
        }
    }

    private fun scaleAnimator(start: Float, end: Float) {
        ObjectAnimator.ofFloat(this, "currentZoom", start, end).apply {
            start()
        }
    }


}