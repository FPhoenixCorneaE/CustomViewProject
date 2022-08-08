package com.example.customviewproject.a.view.a4

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withClip
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: A4ClipView
 * @Author: 史大拿
 * @CreateDate: 8/8/22$ 4:02 PM$
 * TODO 裁剪
 */
class A4ClipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    // 通用bitmap
    private val bitmap by lazy { getBitMap(width / 4) }

    private val paint = Paint()

    companion object {
        private val PADDINT = 20.dp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1.裁剪矩形
        drawClipRect(canvas)

        // 2.根据Rect裁剪 (取反)
        drawClipOutRect(canvas)

        // 3.  根据Path裁剪
        drawClipPath(canvas)

        // 4. 根据Path (取反)
        drawClipOutPath(canvas)

    }

    //region 根据Path (取反)
    /*
     * 作者:史大拿
     * 创建时间: 8/8/22 4:55 PM
     */
    private fun drawClipOutPath(canvas: Canvas) {
        paint.color = Color.RED
        val left = bitmap.width.toFloat() + PADDINT
        val right = bitmap.width.toFloat() + PADDINT + bitmap.width
        val top = bitmap.height + PADDINT
        val bottom = bitmap.height + PADDINT + bitmap.height

        canvas.save() // 保存画布
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val path = Path()
            path.addArc(left, top, right, bottom, -90f, 180f) // 通过path画扇形
            canvas.clipOutPath(path)
        }
        canvas.drawBitmap(bitmap, left, top, paint)
        canvas.restore() // 恢复画布

        paint.color = color()
        canvas.drawRect(left, top, right, bottom, paint)
    }

    //endregion


    //region 根据Path裁剪
    /*
     * 作者:史大拿
     * 创建时间: 8/8/22 4:44 PM
     */
    private fun drawClipPath(canvas: Canvas) {
        paint.color = Color.RED

        val left = 0f
        val right = bitmap.width.toFloat()
        val top = bitmap.height + PADDINT
        val bottom = bitmap.height + PADDINT + bitmap.height

        // 绘制path
        val path = Path()
        path.addArc(left, top, right, bottom, -90f, 180f) // 通过path画扇形
        // 画线，画任何东西都能够裁剪

        canvas.save()
        canvas.clipPath(path)
        canvas.drawBitmap(bitmap, left, top, paint)
        canvas.restore()


        // 绘制辅助矩形
        paint.color = color()
        canvas.drawRect(left, top, right, bottom, paint)
    }

    //endregion

    //region 根据Rect裁剪 (取反)
    /*
     * 作者:史大拿
     * 创建时间: 8/8/22 4:29 PM
     */
    private fun drawClipOutRect(canvas: Canvas) {
        paint.color = Color.RED

        val left = bitmap.width + PADDINT
        val right = bitmap.width + PADDINT + bitmap.width
        val top = 0f
        val bottom = bitmap.height.toFloat()

//
        canvas.save()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            canvas.clipOutRect(left, top, left + (right - left) / 2, bottom / 2) // 取反裁剪
        }
        canvas.drawBitmap(bitmap, left, top, paint)
        canvas.restore()

        paint.color = color()
        canvas.drawRect(left, top, right, bottom, paint)

    }
    //endregion


    //region 裁剪矩形
    /*
     * 作者:史大拿
     * 创建时间: 8/8/22 4:25 PM
     */
    private fun drawClipRect(canvas: Canvas) {
        // java 写法 === start ===
        // 保存画布
//        canvas.save()
//        // 裁剪
//        canvas.clipRect(0, 0, bitmap.width / 2, bitmap.height / 2)
//        // 绘制需要裁剪的图片
//        canvas.drawBitmap(bitmap, 0f, 0f, paint)
//        // 恢复画布
//        canvas.restore()
        // java 写法 === end ===


        // kotlin 写法 === start ===
        canvas.withClip(Rect(0, 0, bitmap.width / 2, bitmap.height / 2)) {
            // 绘制需要裁剪的图片
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }
        // kotlin 写法 === end ===

        // 辅助矩形 (裁剪之前图片大小)
        paint.color = color()
        canvas.drawRect(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
    }
    //endregion

    fun color(alpha: Int = 16) = let {
        Color.argb(alpha, 255, 0, 0)
    }
}