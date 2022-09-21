package com.example.customviewproject.e

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: BaseChartView
 * @Author: 史大拿
 * @CreateDate: 9/21/22$ 11:13 AM$
 * TODO
 */
open class BaseChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        // 网格颜色
        val BACK_COLOR = Color.parseColor("#88888888")
    }

    open val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.strokeWidth = 1.dp
    }


    // 原始数据
    open var originList = arrayListOf<Int>()

    // 水平个数 左侧文字显示个数
    open var horizontalCount = 5

    // 垂直个数
    open var verticalCount: Int = 5

    val originMax: Int
        get() {
            return originList.maxOrNull() ?: 0
        }

    /*
   * 作者:史大拿
   * 创建时间: 9/16/22 3:19 PM
   * TODO 绘制网格
   */
    fun drawGrid(canvas: Canvas) {
        paint.color = BACK_COLOR

        // 绘制垂直线
        val eachWidth = width / verticalCount
        (0 until verticalCount).forEach {
            canvas.drawLine(
                (eachWidth * it).toFloat(),
                0f,
                (eachWidth * it).toFloat(),
                height * 1f,
                paint
            )
        }

        // 绘制水平线
        val eachHeight = height / horizontalCount
        (0 until horizontalCount).forEach {
            canvas.drawLine(
                0f,
                eachHeight * it * 1f,
                width * 1f,
                eachHeight * it * 1f,
                paint
            )
        }

        canvas.drawLine(
            width * 1f,
            0f,
            width * 1f,
            height * 1f,
            paint
        )

        canvas.drawLine(
            0f,
            height * 1f,
            width * 1f,
            height * 1f,
            paint
        )
    }


    /*
     * 作者:史大拿
     * 创建时间: 9/16/22 4:11 PM
     * TODO 绘制文字
     */
    open fun drawLeftText(canvas: Canvas) {
        paint.textSize = 16.dp
        paint.color = Color.BLACK
        // 获取最大值
        val max = originList.maxOrNull()!!
        // 计算每一格的值
        val eachNumber = max / horizontalCount
//        Log.e("szjOriginList", "$originList eachNumber:$eachNumber")

        originList.forEachIndexed { index, _ ->
            val number = max - eachNumber * index
//            Log.e("szj绘制文字", "number:$number\t index:$index\t count:${verticalCount}")

            // 如果number > 0 并且当前不是最后一行
            if (number >= 0 && index != horizontalCount) {
                val text = "$number"
                val rect = Rect()

                // 计算文字宽高
                paint.getTextBounds(text, 0, text.length, rect)
                val textWidth = rect.width()
                val textHeight = rect.height()

                // 计算每一格的高度
                val valueY = height / horizontalCount * index

                val x = -textWidth - 5.dp
                val y = valueY - paint.fontMetrics.top
                canvas.drawText(
                    text,
                    x,
                    y - textHeight,
                    paint
                )
            }
        }
    }
}