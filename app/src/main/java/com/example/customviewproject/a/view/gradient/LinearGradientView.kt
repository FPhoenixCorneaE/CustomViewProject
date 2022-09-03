package com.example.customviewproject.a.view.gradient

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import com.example.customviewproject.ext.dp


/**
 *
 * @ClassName: LinearGradientView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 4:54 PM$
 * TODO
 */
class LinearGradientView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint().apply {
        style = Paint.Style.FILL
        strokeWidth = 2.dp
        color = Color.YELLOW


    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var radius = width / 4f
        /*
         * @param x0,y0: 渐变起点坐标
         * @param x1,y1: 渐变终点坐标
         * @param color1:端点1颜色
         * @param color2:端点2颜色
         * @param mode:
         *  - CLAMP:会 在端点之外延续端点处的颜色
         *  - MIRROR: 镜像模式
         *  - REPEAT: 重复模式
         */
        // TODO 1.=========================
        val shader = LinearGradient(
            100f, 100f, 400f, 400f,
            Color.YELLOW,
            Color.RED,
            Shader.TileMode.CLAMP
        )


        paint.shader = shader
        canvas.drawCircle(width / 4f, width / 4f, radius, paint)

        // TODO 2.=========================
        val shader2 = LinearGradient(
            100f, 100f, 200f, 200f,
            Color.YELLOW,
            Color.RED,
            Shader.TileMode.MIRROR
        )
        paint.shader = shader2
        canvas.drawCircle(width / 4f * 3, width / 4f, radius, paint)

        // TODO 3.=========================
        val shader3 = LinearGradient(
            100f, 100f, 200f, 200f,
            Color.YELLOW,
            Color.RED,
            Shader.TileMode.REPEAT
        )
        paint.shader = shader3
        canvas.drawCircle(width / 4f, width / 4f * 3, radius, paint)

        // TODO 4.=========================
        val cx = width / 4f * 3
        val cy = width / 4f * 3

        val colors = intArrayOf(Color.RED, Color.YELLOW, Color.BLACK)

        /**
         * colors 和 positions参数一样对应
         * TODO 0表示渐变开始 1表示渐变结束
         * 0f-0.3f表示 RED渐变到GREEN占30%
         * 0.3f-1f表示 GREEN渐变BLACK 从30%到结束
         */
        val positions = floatArrayOf(0f, 0.3f, 1f)
        val shader4 = LinearGradient(
            width / 2f,
            cy,
            cx * 2f,
            cy,
            colors, positions, Shader.TileMode.CLAMP
        )
        paint.shader = shader4
        canvas.drawCircle(cx, cy, radius, paint)


        // 辅助线
        canvas.withSave {
            paint.color = Color.BLACK
            paint.shader = null
//            paint.strokeWidth = radius
            drawLine(
                width / 2f,
                cy,
                cx * 2f,
                cy,
                paint
            )
        }


    }
}