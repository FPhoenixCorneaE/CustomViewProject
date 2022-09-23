package com.example.customviewproject.f.f1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.customviewproject.ext.colorRandom

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
        const val COUNT = 200

    }

    val list = arrayListOf<SpeedBean>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        list.clear()


        (0 until COUNT).forEach { _ ->
            list.add(
                SpeedBean(
                    (0 until w).random().toFloat(),
                    (0 until height).random().toFloat(),
                    colorRandom,
                    (5 until 20).random().toFloat()
                )
            )
        }
    }


    override fun onDraw(canvas: Canvas) {
        list.forEach {
            paint.color = it.color
            it.x += it.vX
            it.y += it.vY

            canvas.drawCircle(it.x, it.y, it.radius, paint)

            Log.e("szjItX", it.x.toString())
            when {
                it.x >= right -> {
                    it.vX = (-(5 until 20).random()).toFloat()
                }

                it.x <= left -> {
                    it.vX = ((5 until 20).random()).toFloat()
                }

                it.y <= top -> {
                    it.vY = ((5 until 20).random()).toFloat()
                }

                it.y >= bottom -> {
                    it.vY = (-(5 until 20).random()).toFloat()
                }
            }
        }

        invalidate()
    }


}