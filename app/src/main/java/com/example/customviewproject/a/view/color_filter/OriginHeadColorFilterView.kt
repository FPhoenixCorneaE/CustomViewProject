package com.example.customviewproject.a.view.color_filter

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.R
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: OriginHeadColorFilterView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 4:05 PM$
 * TODO
 */
class OriginHeadColorFilterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val bitmap = getBitMap(R.mipmap.shark15)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(bitmap.width, widthMeasureSpec)
        val height = resolveSize(bitmap.height, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }
}