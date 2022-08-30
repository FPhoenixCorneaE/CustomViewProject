package com.example.customviewproject.a.view.color_filter

import android.content.Context
import android.graphics.Canvas
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.R
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: LightingColorFilterView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 4:05 PM$
 * TODO
 */
class LightingColorFilterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {
        // @param mul: 用来和目标像素相乘，
        // @param add: 用来和目标像素相加
        /*
         *  R' = R * mul.R / 0xff + add.R
            G' = G * mul.G / 0xff + add.G
            B' = B * mul.B / 0xff + add.B
         */

        /**
         * 移除红色:
         * 计算过程:
         * R' = R * 0x0 / 0xff + 0x0 = 0 // 红色被移除
         * G' = G * 0xff / 0xff + 0x0 = G
         * B' = B * 0xff / 0xff + 0x0 = B
         */
        val filter = LightingColorFilter(0x00ffff, 0x000000)

        colorFilter = filter
    }
    val bitmap = getBitMap(R.mipmap.shark15)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
}