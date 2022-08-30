package com.example.customviewproject.a.view.path_effect

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: PathDashPathEffectView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 1:26 PM$
 * TODO
 */
class PathDashPathEffectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        strokeWidth = 4.dp
        style = Paint.Style.STROKE

        // 绘制一个小三角
        val path = Path()
        path.moveTo(20f, 0f)
        path.lineTo(35f, 35f)
        path.lineTo(0f, 35f)
        path.close()

        /*
         * 作者:史大拿
         * 创建时间: 8/30/22 3:45 PM
         * @param path: 需要绘制的东西
         * @param advance: 两个path之间的间距
         * @param phase: 是虚线的偏移
         * @param style:
                    TRANSLATE：位移
                    ROTATE：旋转 (建议在封闭图形内测试) 封闭图形指:圆，三角，矩形等
                    MORPH：变体 (建议在封闭图形内测试) 封闭图形指:圆，三角，矩形等
         */
        pathEffect = PathDashPathEffect(path, 60f, 0f, PathDashPathEffect.Style.TRANSLATE)
    }

    val path by lazy {
        HeadOriginLineView.path
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        canvas.drawPath(path, paint)
    }
}