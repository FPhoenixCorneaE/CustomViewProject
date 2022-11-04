package com.example.customviewproject.a.view.path_measure

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.screenWidth


/**
 *
 * @ClassName: PathMeasureView
 * @Author: 史大拿
 * @CreateDate: 11/1/22$ 3:15 PM$
 * TODO 参考文档:https://cloud.tencent.com/developer/article/2027721
 */
class PathMeasureNextContourView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val path by lazy {
        Path().apply {
            var temp = 0f

            // 绘制5调线
            (0 until 5).forEach { _ ->
                temp += 100f
                moveTo(temp, temp)
                lineTo(screenWidth - temp, temp)
            }
        }
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
            style = Paint.Style.STROKE
            strokeWidth = 5.dp
        }

    private val pathMeasure by lazy {
        PathMeasure(path, false)
    }


    override fun onDraw(canvas: Canvas) {

        paint.color = Color.BLACK
        canvas.drawPath(path, paint)

        Log.i("szjLength1", "${pathMeasure.length}")

        /*
         * 作者:史大拿
         * 创建时间: 11/1/22 5:05 PM
         * TODO path可以由多条曲线构成，但是无论是getLength,getSegment() 或者其他方法，都只是获取到第一条线段上运行
         *  ，那么nextContour() 就是跳转到吓一条线段
         *
         * return true: 跳转成功
         * return false: 跳转失败
         */

        // 跳转到下一条线
        if (pathMeasure.nextContour()) {
            // 打印长度
            Log.i("szjLength2", "${pathMeasure.length}")
        }

        if (pathMeasure.nextContour()) {
            // 打印长度
            Log.i("szjLength3", "${pathMeasure.length}")
        }

        if (pathMeasure.nextContour()) {
            // 打印长度
            Log.i("szjLength4", "${pathMeasure.length}")
        }

        if (pathMeasure.nextContour()) {
            // 打印长度
            Log.i("szjLength5", "${pathMeasure.length}")
        }

        Log.i("szjLength6", "${pathMeasure.nextContour()}")

    }
}