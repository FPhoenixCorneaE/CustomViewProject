package com.example.customviewproject.a.view.a6

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import androidx.core.graphics.withTranslation
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap

/**
 *
 * @ClassName: A6CameraView
 * @Author: 史大拿
 * @CreateDate: 8/8/22$ 7:15 PM$
 * TODO
 */
class A6CameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        strokeWidth = 4.dp
    }

    private val camera = Camera().also {
        // 基本认知
        it.rotateX(20f)


        /*
         * 作者:史大拿
         * 创建时间: 8/8/22 7:37 PM
         * @param x: x轴方向拉
         * @param y: y轴方向拉
         * @param z: z轴方向拉 默认为 -8 英寸(1英寸 = 72像素)
         * context.resources.displayMetrics.density = 像素密度
         */
        it.setLocation(0f, 0f, -6f * context.resources.displayMetrics.density)
    }

    private val bitMap by lazy { getBitMap(width / 2) }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 基本认识
//        drawTest1(canvas)

        drawFlip(canvas)

    }


    //region
    /*
     * 作者:史大拿
     * 创建时间: 8/8/22 7:46 PM
     */
    private fun drawFlip(canvas: Canvas) {
        val left = width / 2f - bitMap.width / 2f
        val top = height / 2f - bitMap.height / 2f
        val right = left + bitMap.width
        val bottom = top + bitMap.height

        // TODO 绘制第一张平的图片
        canvas.withSave {
            canvas.clipRect(left, top, right, (bottom + top) / 2) // 裁剪

            canvas.drawBitmap(bitMap,
                left,
                top,
                paint)
        }
        // TODO 绘制第二张弯的图
        canvas.withSave {

            // 5. 将图片移动到原来位置
            canvas.translate(left + bitMap.width / 2, top + bitMap.height / 2)

            // 4. 将图片搞弯
            camera.applyToCanvas(canvas)

            // 3.将图片中心点 平移到起始点(0,0)
            canvas.translate(-(left + bitMap.width / 2), -(top + bitMap.height / 2))

            // 2. 裁剪
            canvas.clipRect(left, top + bitMap.height / 2, right, bottom)

            // 1. 绘制图片
            canvas.drawBitmap(bitMap,
                left,
                top,
                paint)
        }

    }

    //endregion


    //region 基本认识
    /*
     * 作者:史大拿
     * 创建时间: 8/8/22 7:29 PM
     */

    private fun drawTest1(canvas: Canvas) {
        val left = width / 2f - bitMap.width / 2f
        val top = height / 2f - bitMap.height / 2f

        canvas.save()
        // 4。将画布移动回去
        canvas.translate(left + bitMap.width / 2, top + bitMap.width / 2)

        // 3。x轴平移
        camera.applyToCanvas(canvas)

        // 2。将画布移动到左上角
        canvas.translate(-(left + bitMap.width / 2), -(top + bitMap.width / 2))

        // 1。居中图片
        canvas.drawBitmap(bitMap,
            left,
            top,
            paint)
        canvas.restore()
    }
//endregion

}