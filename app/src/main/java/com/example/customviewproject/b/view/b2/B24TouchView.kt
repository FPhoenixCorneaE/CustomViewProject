package com.example.customviewproject.b.view.b2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import androidx.core.util.forEach
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: B2PointView
 * @Author: 史大拿
 * @CreateDate: 8/12/22$ 7:28 PM$
 * TODO 多点触控 画板多点触控
 */
class B24TouchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        strokeWidth = 6.dp
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val pathMap = SparseArray<Path>()


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                // 获取当前按压的id
                val id = event.getPointerId(event.actionIndex)

                val path = Path()
                path.moveTo(event.getX(event.actionIndex), event.getY(event.actionIndex))

                // id不会变，所以使用id来记录
                // 直接添加
                pathMap.append(id, path)
            }
            MotionEvent.ACTION_MOVE -> {

                var index = 0
                pathMap.forEach { _, value ->
                    // 绘制
                    value.lineTo(event.getX(index), event.getY(index))
                    index++
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                // 删除当前抬起的key
                pathMap.remove(event.getPointerId(event.actionIndex))
            }
        }

        invalidate() // 刷新

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        pathMap.forEach { _, value ->
            canvas.drawPath(value, paint)
        }
    }

}