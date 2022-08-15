package com.example.customviewproject.b.view.b2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: B2PointView
 * @Author: 史大拿
 * @CreateDate: 8/15/22$
 * TODO 多点触控 抢夺型
 */
class B21TouchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        strokeWidth = 6.dp
    }

    // 偏移量
    private val offSet by lazy {
        // 默认图片居中
        PointF(width / 2f - bitmap.width / 2f, height / 2f - bitmap.height / 2f)
    }

    // 按压时候(DOWN)的偏移
    private val downOffset = PointF(0f, 0f)


    // 初始偏移
    private val originOffset = PointF(0f, 0f)


    // 图片
    private val bitmap = getBitMap()

    // 记录当前点击事件的id
    private var currentPointId = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        /**
         * event.pointerCount 获取所有指头数
         * event.getX() 获取到第0个指头位置
         * event.getX(index:Int) 获取到第Int根指头的位置
         * event.actionIndex 当前DOWN/UP 手指index (tips:在ACTION_MOVE中始终返回0)
         * event.deviceId 手指id 【按下，移动，抬起，都是唯一的】
         * event.findPointerIndex(id:Int) 传入id获取到此次事件对应的index
         * event.getPointerId(pointerIndex:Int) 传入当前手指的index 获取对应的id
         */

        /**
         * event.actionMasked 和 event.action 的区别:
         * event.actionMasked 会响应多点事件
         * event.action 不会响应多点事件
         */
        when (event.actionMasked) {
            // 第一根手指按下的时候响应
            MotionEvent.ACTION_DOWN -> {
                // 记录按压位置
                downOffset.x = event.x
                downOffset.y = event.y

                // 记录原始偏移量
                originOffset.x = offSet.x
                originOffset.y = offSet.y
                Log.e("szjACTION_DOWN", "${event.actionIndex}")

                // down事件默认index = 0
                currentPointId = event.getPointerId(0)
            }

            /**
             * 在多个手指按下的时候响应
             */
            MotionEvent.ACTION_POINTER_DOWN -> {
                currentPointId = event.getPointerId(event.actionIndex)

                // 记录按压位置
                downOffset.x = event.getX(event.actionIndex)
                downOffset.y = event.getY(event.actionIndex)

                // 记录原始偏移量
                originOffset.x = offSet.x
                originOffset.y = offSet.y

                Log.e("szjACTION_POINTER_DOWN",
                    "id = ${currentPointId}\tindex\t${event.actionIndex}\tcount:${event.pointerCount}")
            }

            MotionEvent.ACTION_MOVE -> {
                // 当前偏移量 = 当前位置 - 按压位置 + 原始偏移量
                val index = event.findPointerIndex(currentPointId)
                offSet.x = event.getX(index) - downOffset.x + originOffset.x
                offSet.y = event.getY(index) - downOffset.y + originOffset.y
                Log.e("szjACTION_MOVE", "$event")
            }

            // 非最后一根手指抬起时响应
            MotionEvent.ACTION_POINTER_UP -> {
                // 当前手指抬起的index
                val index = event.actionIndex
                // 获取抬起手指的id
                val pointId = event.getPointerId(index)

                Log.e("szjACTION_POINTER_UP", "${event.actionIndex}")
                // 如果当前抬起手指的id == 当前手指的id; 就让最后一根手指来 "指挥"
                if (pointId == currentPointId) {

                    // 判断当前手指是否是最后一根手指
                    val newIndex =
                        if (index == event.pointerCount - 1) {
                            // 如果是最后一根，就用倒数第二根手指来 "指挥"
                            event.pointerCount - 2
                        } else {
                            event.pointerCount - 1
                        }

                    // 重新记录当前id
                    currentPointId = event.getPointerId(newIndex)

                    // 记录按压位置
                    downOffset.x = event.getX(newIndex)
                    downOffset.y = event.getY(newIndex)

                    // 记录原始偏移量
                    originOffset.x = offSet.x
                    originOffset.y = offSet.y

                }

                Log.e("szjACTION_POINTER_UP", "${event.actionIndex}")
            }

            // 最后一根手指抬起时候响应
            MotionEvent.ACTION_UP -> {

                Log.e("szjACTION_UP", "${event.actionIndex}")
            }
        }

        invalidate() // 刷新
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap,
            offSet.x,
            offSet.y, paint)
    }
}