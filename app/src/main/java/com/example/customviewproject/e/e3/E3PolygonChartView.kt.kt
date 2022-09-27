package com.example.customviewproject.e.e3

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.example.customviewproject.ext.dp
import java.util.*
import kotlin.math.*

/**
 *
 * @ClassName: E3PolygonChartView
 * @Author: 史大拿
 * @CreateDate: 9/21/22$ 3:49 PM$
 * TODO 多边形图表 本文fling事件参考自MPAndroidChart
 */
open class E3PolygonChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        // 蛛网背景颜色
        val POLYGON_BACK_COLOR = Color.parseColor("#88888888")

        // 文字颜色
        val TEXT_COLOR = Color.parseColor("#333333")

        // 最小多边形 (中间最小多边形大小)
        val MIN_RADIUS = 20.dp

        // 多边形间隔
        val INTERVAL = 20.dp

        // 滑动速度【0-1】
        const val SPEED = 0.9f
    }

    // 点数
    private val count: Int
        get() {
            return data.size
        }

    // 多边形条数
    private val polygonCount: Int
        get() {
            // 寻找到最大值 设置为变数
            return data.map { it.second }.toList().maxOrNull()?.toInt() ?: 5
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val path = Path()


    /*
     * 作者:史大拿
     * 创建时间: 9/22/22 9:38 AM
     * TODO 设置数据
     */
    open var data = arrayListOf<Pair<String, Float>>()

    private val centerLocation by lazy {
        PointF(width / 2f, height / 2f)
    }

    // 当前进度
    private var currentFraction = 0f

    private val startAnimator by lazy {
        val animator = ObjectAnimator.ofFloat(0f, 1f)
        animator.duration = 1000

        animator.addUpdateListener {
            currentFraction = it.animatedFraction


            invalidate()
        }

        animator
    }

    init {
        startAnimator.start()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = resolveSize(measuredWidth, widthMeasureSpec)
        val height = resolveSize(width, heightMeasureSpec)
        Log.e("onMeasure", "width:$width\tmeasuredWidth:$measuredWidth")
        setMeasuredDimension(width, height)
    }

    private var angle = 0f
    private var mStartAngle = 0f
    private var originAngle = 0f

    private var mTouchStartPoint = PointF()
    private var mDecelerationAngularVelocity = 0f
    private var mDecelerationLastTime: Long = 0

    private class AngularVelocitySample(var time: Long, var angle: Float)

    private val _velocitySamples = ArrayList<AngularVelocitySample>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        sampleVelocity(event.x, event.y)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDecelerationAngularVelocity = 0f

                _velocitySamples.clear()

                mTouchStartPoint.x = event.x
                mTouchStartPoint.y = event.y

                mStartAngle = getAngleForPoint(event.x, event.y)
                originAngle = angle
            }
            MotionEvent.ACTION_MOVE -> {

                angle = getAngleForPoint(event.x, event.y) - mStartAngle + originAngle
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                mDecelerationAngularVelocity = 0f
                mDecelerationAngularVelocity = calculateVelocity()

                if (mDecelerationAngularVelocity != 0f) {
                    mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis()
                    postInvalidateOnAnimation()
                }
            }
        }
//
        return true
    }

    override fun computeScroll() {
        if (mDecelerationAngularVelocity == 0f) return  // There's no deceleration in progress

        if (_velocitySamples.size == 0) return

        val currentTime = AnimationUtils.currentAnimationTimeMillis()

        mDecelerationAngularVelocity *= SPEED

        val timeInterval = (currentTime - mDecelerationLastTime).toFloat() / 1000f

        angle += mDecelerationAngularVelocity * timeInterval
        Log.e("szjAngle", "$angle")

//        mChart.setRotationAngle(mChart.getRotationAngle() + mDecelerationAngularVelocity * timeInterval)

        mDecelerationLastTime = currentTime

        if (abs(mDecelerationAngularVelocity) >= 0.001) {
            postInvalidateOnAnimation()
        } else {
            mDecelerationAngularVelocity = 0f
        }
    }

    private fun calculateVelocity(): Float {
        if (_velocitySamples.isEmpty()) return 0f
        val firstSample = _velocitySamples[0]
        val lastSample = _velocitySamples[_velocitySamples.size - 1]

        // Look for a sample that's closest to the latest sample, but not the same, so we can deduce the direction
        var beforeLastSample = firstSample
        for (i in _velocitySamples.indices.reversed()) {
            beforeLastSample = _velocitySamples[i]
            if (beforeLastSample.angle != lastSample.angle) {
                break
            }
        }

        // Calculate the sampling time
        var timeDelta = (lastSample.time - firstSample.time) / 1000f
        if (timeDelta == 0f) {
            timeDelta = 0.1f
        }

        // Calculate clockwise/ccw by choosing two values that should be closest to each other,
        // so if the angles are two far from each other we know they are inverted "for sure"
        var clockwise = lastSample.angle >= beforeLastSample.angle
        if (abs(lastSample.angle - beforeLastSample.angle) > 270.0) {
            clockwise = !clockwise
        }

        // Now if the "gesture" is over a too big of an angle - then we know the angles are inverted, and we need to move them closer to each other from both sides of the 360.0 wrapping point
        if (lastSample.angle - firstSample.angle > 180.0) {
            firstSample.angle += 360.0f
        } else if (firstSample.angle - lastSample.angle > 180.0) {
            lastSample.angle += 360.0f
        }

        // The velocity
        var velocity = abs((lastSample.angle - firstSample.angle) / timeDelta)

        // Direction?
        if (!clockwise) {
            velocity = -velocity
        }
        return velocity
    }

    private fun sampleVelocity(touchLocationX: Float, touchLocationY: Float) {
        val currentTime = AnimationUtils.currentAnimationTimeMillis()
        _velocitySamples.add(
            AngularVelocitySample(
                currentTime,
                getAngleForPoint(touchLocationX, touchLocationY)
            )
        )

        var i = 0
        var count = _velocitySamples.size
        while (i < count - 2) {
            if (currentTime - _velocitySamples[i].time > 1000) {
                _velocitySamples.removeAt(0)
                i--
                count--
            } else {
                break
            }
            i++
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/22/22 1:19 PM
     * TODO 根据当前位置计算角度
     *
     * 粘贴自MPAndroidChart.java 也可以查看PointFExt#angle() 方法
     */
    private fun getAngleForPoint(x: Float, y: Float): Float {
        val cx = centerLocation.x
        val cy = centerLocation.y
//        Log.i("szjCx", "cx:" + c.getX().toString() + "\t" + "y:" + c.y)
        val tx = x - cx
        val ty = y - cy
        val length = sqrt((tx * tx + ty * ty).toDouble())

        val r = acos(ty / length)

        var angle = Math.toDegrees(r).toFloat()

        if (x > cx) angle = 360f - angle

        // add 90° because chart starts EAST
        angle += 90f

        // neutralize overflow
        if (angle > 360f) angle -= 360f
        return angle
    }

    override fun onDraw(canvas: Canvas) {

        // 绘制背景多边形
        drawBackPolygon(canvas)


        // 绘制具体区域
        drawArea(canvas)
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/22/22 9:58 AM
     * TODO 绘制选中区域
     */
    private fun drawArea(canvas: Canvas) {
        data.forEachIndexed { index, value ->
            val location = getLocation(index, value.second)

            if (index == 0) {
                path.moveTo(location.x, location.y)
            } else {
                path.lineTo(location.x, location.y)
            }
        }
        path.close()


        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        canvas.drawPath(path, paint)

        paint.style = Paint.Style.FILL
        paint.alpha = (255 * 0.5).toInt()
        canvas.drawPath(path, paint)
        path.reset()
    }


    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 7:11 PM
     * TODO 获取对应位置
     * @number : 第几个点
     * @score: 第几条边
     * @return PointF: <x X坐标> <y Y坐标>
     */
    private fun getLocation(number: Int, score: Float): PointF = let {
        // 角度
        val angle = 360 / count * number + angle
        // 半径
        val radius = (score - 1) * INTERVAL + MIN_RADIUS * currentFraction
        val x =
            (radius * cos(Math.toRadians(angle.toDouble())) + centerLocation.x).toFloat()
        val y =
            (radius * sin(Math.toRadians(angle.toDouble())) + centerLocation.y).toFloat()

        return PointF(x, y)
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/21/22 4:11 PM
     * TODO 绘制背景多边形
     */
    private fun drawBackPolygon(canvas: Canvas) {
//        paint.color = Color.RED
//        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)
        val eachAngle = 360 / count
        (0 until polygonCount).forEachIndexed { index, element ->

            (0 until count).forEach { count ->
                // 当前是第几条 * 间隔 + 最小半径 = 当前半径
                val radius = element * INTERVAL + MIN_RADIUS
                // 角度
                val angle = count * eachAngle.toDouble() + angle
                val x =
                    (radius * cos(Math.toRadians(angle)) + centerLocation.x).toFloat()
                val y =
                    (radius * sin(Math.toRadians(angle)) + centerLocation.y).toFloat()

//                canvas.drawCircle(x, y, SMALL_RADIUS, paint)
                if (count == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }

                // 设置文字
                if (index == polygonCount - 1) {
                    val text = data[count].first

                    val rect = Rect()

                    paint.getTextBounds(text, 0, text.length, rect)
                    val textWidth = rect.width()
                    val textHeight = rect.height()

                    val tempRadius = radius + textHeight
                    val textX =
                        (tempRadius * cos(Math.toRadians(angle)) + centerLocation.x).toFloat() - textWidth / 2f
                    val textY =
                        (tempRadius * sin(Math.toRadians(angle)) + centerLocation.y).toFloat()

                    paint.textSize = 16.dp
                    paint.style = Paint.Style.FILL
                    paint.color = TEXT_COLOR
                    canvas.drawText(text, textX, textY, paint)

//                    canvas.drawCircle(textX, textY, 5.dp, paint)
                }


                // 设置最外层和最中间的连接线
                if (index == polygonCount - 1) {
                    paint.color = POLYGON_BACK_COLOR
                    canvas.drawLine(x, y, centerLocation.x, centerLocation.y, paint)
                }
            }

            path.close()

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2.dp
            paint.color = POLYGON_BACK_COLOR
            canvas.drawPath(path, paint)
            path.reset()
        }
    }


}