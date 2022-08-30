package com.example.customviewproject.a.view.color_filter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.R
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: ColorMatrixColorFilterView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 4:05 PM$
 * TODO
 */
class ColorMatrixColorFilterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {

        /**
         * ColorMatrix 内部是一个4X5的矩阵:
         * [ a, b, c, d, e,  f, g, h, i, j,  k, l, m, n, o,  p, q, r, s, t ]
         *
         * 算法为:
         * R’ = a*R + b*G + c*B + d*A + e;
         * G’ = f*R + g*G + h*B + i*A + j;
         * B’ = k*R + l*G + m*B + n*A + o;
         * A’ = p*R + q*G + r*B + s*A + t;
         */

        val colorMatrix = ColorMatrix()
        // 设置饱和度 [0..1]
        colorMatrix.setSaturation(0.3f)
        val filter = ColorMatrixColorFilter(colorMatrix)

        colorFilter = filter
    }
    val bitmap = getBitMap(R.mipmap.shark15)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
}