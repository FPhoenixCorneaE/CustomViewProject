package com.example.customviewproject.f.f1

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.*
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @ClassName: F11View
 * @Author: 史大拿
 * @CreateDate: 9/23/22$ 7:58 PM$
 * TODO
 */
class F13View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    companion object {

        // 总显示个数
        const val COUNT = 120

        // 连接半径
        val RADIUS = 100.dp
    }

    // 半径(px)
    private val radiusPX = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX, RADIUS,
        Resources.getSystem().displayMetrics
    )

    val list = arrayListOf<SpeedBean>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        list.clear()


        (0 until COUNT).forEach { _ ->
            list.add(
                SpeedBean(
                    (0 until w).random().toFloat(),
                    (0 until h).random().toFloat(),
                    colorRandom,
                    (5 until 20).random().toFloat(),
//                    radius = 20f,// 半径
                    vX = (-5 until 5).random().toFloat(), // 初始化原始偏移量
                    vY = (-5 until 5).random().toFloat(),
                    collisionWear = (5 until 9).random() / 10f,
                )
            )
        }
    }

    private val movePointF = PointF(-RADIUS, -RADIUS)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)

                movePointF.x = x
                movePointF.y = y
                list.forEach {
                    if (PointF(x, y).contains(PointF(it.x, it.y), RADIUS)
                    ) {
                        if (it.angle == 0f) {
                            // 偏移角度
//                            val angle = (PointF(x, y)).angle2(PointF(it.x, it.y))
                            val angle = (PointF(x, y)).angle(PointF(it.x, it.y))

                            it.angle = angle
                        }
                        Log.e("szjAngle", "${it.angle.toDouble()}")

                        it.x = (x + RADIUS * cos(Math.toRadians(it.angle.toDouble()))).toFloat()
                        it.y = (y + RADIUS * sin(Math.toRadians(it.angle.toDouble()))).toFloat()

                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                movePointF.x = -RADIUS
                movePointF.y = -RADIUS
                list.forEach {
                    it.angle = 0f
                }
            }
        }

        postInvalidateOnAnimation()
        return true
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        paint.color = Color.RED
        paint.alpha = (255 * 0.1f).toInt()
        canvas.drawCircle(movePointF.x, movePointF.y, RADIUS, paint)

        list.forEach {
            paint.color = it.color

            canvas.drawCircle(it.x, it.y, it.radius, paint)
            list.forEach { value ->
                if (PointF(value.x, value.y).contains(PointF(it.x, it.y), RADIUS)
                ) {
                    paint.strokeWidth = 1.dp
                    paint.color = value.color

                    // 计算距离
                    val d = PointF(value.x, value.y).distance(PointF(it.x, it.y))

                    paint.alpha = (d / radiusPX * 255f).toInt()
                    if (PointF(movePointF.x, movePointF.y).contains(PointF(it.x, it.y), RADIUS)
                    ) {
                        // TODO 触摸
                        canvas.drawLine(value.x, value.y, movePointF.x, movePointF.y, paint)
                    } else {
                        // 每个点之间的连接线
                        canvas.drawLine(value.x, value.y, it.x, it.y, paint)
                    }
                }
            }

            // 判断是否触摸中
            // 如果没有触摸中一直移动
            if (!PointF(movePointF.x, movePointF.y).contains(PointF(it.x, it.y), RADIUS)
            ) {
                it.x += it.vX
                it.y += it.vY
            }
            canvas.drawCircle(it.x, it.y, it.radius, paint)
            when {
                it.x >= right -> {
                    it.vX = -it.randomV.toFloat()
                }

                it.x <= left -> {
                    it.vX = it.randomV.toFloat()
                }

                it.y <= top -> {
                    it.vY = it.randomV.toFloat()
                }

                it.y >= bottom -> {
                    it.vY = -it.randomV.toFloat()
                }
            }
        }

        postInvalidateOnAnimation()
    }
}