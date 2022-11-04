package com.example.customviewproject.a.view.path_measure

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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
class PathMeasureSegmentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val path = Path().apply {
        moveTo(100f, 100f)
        lineTo(screenWidth - 100f, 100f)
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
            style = Paint.Style.STROKE
            strokeWidth = 5.dp
        }

    private val pathMeasure = PathMeasure(path, false)


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        paint.color = Color.BLACK
        canvas.drawPath(path, paint)

        val pa2 = Path()
        /*
         * 作者:史大拿
         * 创建时间: 11/1/22 4:44 PM
         * TODO 用于截取Path
         * @startD: 开始点
         * @stopD:结束点
         * @path: 截取的 Path 将会添加到 dst 中 (注意: 是添加，而不是替换)
         * @startWithMoveTo: 起始点是否使用 moveTo
         *
         * tips: 如果 startD、stopD 的数值不在取值范围 [0, getLength] 内，或者 startD == stopD 则返回值为 false，不会改变 dst 内容。
         */
        val segment = pathMeasure.getSegment(0f, pathMeasure.length * 0.5f, pa2, true)

        if (segment) {
            paint.color = Color.RED
            canvas.drawPath(pa2, paint)
        }
    }
}