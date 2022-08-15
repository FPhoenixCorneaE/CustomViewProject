package com.example.customviewproject.b.view.b2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: B2PointView
 * @Author: 史大拿
 * @CreateDate: 8/12/22$ 7:28 PM$
 * TODO 多点触控 默认类型
 */
class B20TouchView @JvmOverloads constructor(
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录按压位置
                downOffset.x = event.x
                downOffset.y = event.y

                // 记录原始偏移量
                originOffset.x = offSet.x
                originOffset.y = offSet.y
            }
            MotionEvent.ACTION_MOVE -> {
                // 当前偏移量 = 当前位置 - 按压位置 + 原始偏移量
                offSet.x = event.x - downOffset.x + originOffset.x
                offSet.y = event.y - downOffset.y + originOffset.y
            }
            MotionEvent.ACTION_UP -> {

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