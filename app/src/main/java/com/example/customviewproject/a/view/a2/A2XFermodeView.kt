package com.example.customviewproject.a.view.a2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.R

/**
 *
 * @ClassName: XFermodeView
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 1:47 PM$
 * TODO 参考文档: https://developer.android.com/reference/android/graphics/PorterDuff.Mode
 */
class A2XFermodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    // 需要裁剪的图片
    private val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.user)

    private val paint = Paint()

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val rect = RectF(20f, 20f, bitmap.width + 20f, bitmap.height + 20f)
        // 离线缓存
        val count = canvas.saveLayer(rect, null)
//        canvas.drawRoundRect(rect,
//            20f,
//            20f,
//            paint)

        canvas.drawOval(rect,
            paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 20f, 20f, paint)
        paint.xfermode = null
        canvas.restoreToCount(count)
    }
}