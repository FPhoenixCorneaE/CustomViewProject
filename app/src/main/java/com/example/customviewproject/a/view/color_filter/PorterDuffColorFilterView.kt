package com.example.customviewproject.a.view.color_filter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.R
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: PorterDuffColorFilterView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 4:05 PM$
 * TODO 参考文档: https://juejin.cn/post/6844903487570968584#heading-6
 */
class PorterDuffColorFilterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {

        // 这个filter没啥用。。。
        val filter = PorterDuffColorFilter(0x00ffff, PorterDuff.Mode.OVERLAY)

        colorFilter = filter
    }
    val bitmap = getBitMap(R.mipmap.shark15)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
}