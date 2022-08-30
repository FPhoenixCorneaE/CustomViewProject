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
 * TODO 辐射渐变
 */
class RadialGradientView @JvmOverloads constructor(
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
         * 作者:史大拿
         * 创建时间: 8/30/22 5:22 PM
         * @param centerX,centerY: 渐变中心位置
         * @param radius: 渐变范围
         * @param color1: 辐射边缘颜色
         * @param color2: 辐射中心颜色
         * @param mode:
         *  - CLAMP: 在端点之外延续端点处的颜色
         *  - MIRROR: 镜像模式
         *  - REPEAT: 重复模式
         */
        val shader = RadialGradient(
            100f, 100f,
            300f, Color.RED, Color.YELLOW, Shader.TileMode.CLAMP
        )
        paint.shader = shader
        canvas.drawCircle(width / 4f, width / 4f, width / 4f, paint)


        val shader2 = RadialGradient(
            300f, 300f,
            300f, Color.RED, Color.YELLOW, Shader.TileMode.MIRROR
        )
        paint.shader = shader2
        canvas.drawCircle(width / 4f * 3, width / 4f, width / 4f, paint)


        val shader3 = RadialGradient(
            300f, 300f,
            300f, Color.RED, Color.YELLOW, Shader.TileMode.REPEAT
        )
        paint.shader = shader3
        canvas.drawCircle(width / 4f, width / 4f * 3, width / 4f, paint)
    }
}