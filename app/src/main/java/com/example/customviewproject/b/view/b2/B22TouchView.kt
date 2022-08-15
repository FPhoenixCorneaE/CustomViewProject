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
 * @CreateDate: 8/12/22$ 13:58
 * TODO 多点触控 焦点型
 */
class B22TouchView @JvmOverloads constructor(
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

    // 焦点坐标
    private val focusPointF = PointF(0f, 0f)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        // 记录所以按下的sum值
        var sumX = 0f
        var sumY = 0f
        // 判断当前是否是 ACTION_POINTER_UP事件
        val isPointUp = MotionEvent.ACTION_POINTER_UP == event.actionMasked
//        Log.e("szjOnTouchEvent","${event.actionIndex}")

        (0 until event.pointerCount).forEach {
            // 如果当前是pointer_up事件 并且 当前事件为最后一个抬起 那么就累加
            if (!(isPointUp && it == event.actionIndex)) {
                sumX += event.getX(it)
                sumY += event.getY(it)
            }
        }

        var pointCount = event.pointerCount
        // 如果是point_up事件，那么就不计算最后一个up事件坐标
        if (isPointUp) {
            pointCount -= 1
        }
        // 计算焦点位置
        focusPointF.x = sumX / pointCount
        focusPointF.y = sumY / pointCount

        when (event.actionMasked) {
            // 第一根手指按下的时候响应
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                // 记录按压位置
                downOffset.x = focusPointF.x
                downOffset.y = focusPointF.y

                // 记录原始偏移量
                originOffset.x = offSet.x
                originOffset.y = offSet.y
                Log.e("szjACTION_DOWN", "${event.actionIndex}")
            }


            MotionEvent.ACTION_MOVE -> {
                // 当前偏移量 = 当前位置 - 按压位置 + 原始偏移量
                offSet.x = focusPointF.x - downOffset.x + originOffset.x
                offSet.y = focusPointF.y - downOffset.y + originOffset.y
                Log.e("szjACTION_MOVE", "$event")
            }

            // 最后一根手指抬起时候响应
            MotionEvent.ACTION_UP -> {
                Log.e("szjACTION_UP", "${event.actionIndex}")
            }
        }

        invalidate() // 刷新
        return true
    }

    /**
     *
    当前效果:
     * 手指1按下，不松手; 手指2按下时，手指2移动不起效果，
     * 当手指1抬起，手指2会立马接手到事件，图片会瞬间移动到手指2的位置
     *
     * 若手指2不松手，再次按下手指1的时候，手指1会立马抢去"指挥权"
     * ==============================================================
     *
    出现原因:
     * 当手指1按下时候，手指1的event.actionIndex = 0,手指1不松开，
     * 手指2按下的时候，手指2的event.actionIndex = 1，手指2不松开，
     * 此时屏幕上有2根手指和2个事件
     * 若此时手指1松手，event.actionIndex = 0事件消失，手指2的event.actionIndex
     * 此时屏幕上有1根手（手指2）指和1个事件(event.actionIndex = 1)
     *
     * 若此时出现手指3,那么手指3的event.actionIndex = 0
     * event.x / event.y 获取的是 event.actionIndex = 0 位置的坐标，所以会出现抢"指挥权"的情况
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap,
            offSet.x,
            offSet.y, paint)
    }
}