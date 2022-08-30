package com.example.customviewproject.a.view.gradient

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
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
        val shader = LinearGradient(
            100f, 100f, 400f, 400f,
            Color.YELLOW,
            Color.RED,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        canvas.drawCircle(width / 4f, width / 4f, width / 4f, paint)


        val shader2 = LinearGradient(
            100f, 100f, 200f, 200f,
            Color.YELLOW,
            Color.RED,
            Shader.TileMode.MIRROR
        )
        paint.shader = shader2
        canvas.drawCircle(width / 4f * 3, width / 4f, width / 4f, paint)


        val shader3 = LinearGradient(
            100f, 100f, 200f, 200f,
            Color.YELLOW,
            Color.RED,
            Shader.TileMode.REPEAT
        )
        paint.shader = shader3
        canvas.drawCircle(width / 4f, width / 4f * 3, width / 4f, paint)
    }
}