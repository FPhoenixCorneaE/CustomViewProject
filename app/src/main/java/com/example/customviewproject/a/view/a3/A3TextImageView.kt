package com.example.customviewproject.a.view.a3

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.customviewproject.R
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: A3TextImageView
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 4:50 PM$
 * TODO 图片文字
 */
class A3TextImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val text =
        """关前取音见得算存合，好出资劳处越果论，铁明W取据张接.究场体增身通置人外他片，千得这还使次深走已青维格，对高肃困还难6特花论。 装内需上率气型她，线族清然大力造事，究A号后V样。 利由见院连特立机快很市响，算根格流建B别4重别。 支立王米确做次法，万全广学老装，色格刷果私技。
      活与据参民个权色收观金，它更可想交改具代界电，市常H养英同步求研。 快了于革音权取认样不，机管着政活联教构低，子气5林群活二接。 候之离统育到例调军重，农着要压各增长六，增地Y业杯拉身旷。 热理工清没主斯达油看集京，律积色采省律见代管九，飞约屈后名业分想N。 市领据形术图会最研白，海高局成市土表从，度今屈养我意府步。
    """.trimIndent()

    // 需要绘制的图片
    private val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.user)

    // 图片偏移量
    private val bitMapOffSetY = 40.dp


    private val paint = Paint().apply {
        textSize = 14.dp
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        drawTest(canvas)

        // 绘制图片
        drawBitMap(canvas)

        var count: Int // 当前一行有多少个字符
        var start = 0 // 开始位置

        var offSetY = -paint.fontMetrics.top // 当前偏移量

        //
        while (start < text.length) {

            // 当前偏移量 < 图片偏移量 说明在顶部 不需要换行
            // 或者 当前偏移量 > 图片偏移量 + 图片高度 说明在底部 不需要换行
            val maxWidth = if (offSetY < bitMapOffSetY || offSetY > bitMapOffSetY + bitmap.height) {
                width.toFloat()
            } else {
                // 说明在有图片存在，需要减去图片宽度
                width.toFloat() - bitmap.width
            }

            count = paint.breakText(text, start, text.length, true, maxWidth, floatArrayOf())
            canvas.drawText(text, start, start + count, 0f, offSetY, paint)

            // 每循环一行，记录值
            start += count

            // 每次记录行高
            offSetY += paint.fontSpacing
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 8/6/22 5:20 PM
     * TODO 绘制图片
     */
    private fun drawBitMap(canvas: Canvas) {
        canvas.drawBitmap(bitmap, width - bitmap.width.toFloat(), bitMapOffSetY, paint)
    }

    /*
     * 作者:史大拿
     * 创建时间: 8/6/22 5:20 PM
     * TODO 测试 每一行如何计算
     */
    private fun drawTest(canvas: Canvas) {
        val fontMetrics = paint.fontMetrics

        val floats = floatArrayOf()

        /*
         * 作者:史大拿
         * 创建时间: 8/6/22 5:01 PM
         * @param1: 文字
         * @param2: 是否是从前往后测量
         * @param3: 最大宽度(多久换行)
         * return:一整行有多少个字符
         */
        // TODO 绘制第一行
        var count = paint.breakText(text, true, width.toFloat(), floats)
        Log.i("szj文字1", "count:$count")
        canvas.drawText(text, 0, count, 0f, -fontMetrics.top, paint)


        val tempCount = count
        // TODO 绘制第二行
        /*
         * 作者:史大拿
         * 创建时间: 8/6/22 5:10 PM
         * @param1: 文字
         * @param2: 文字开始计算位置
         * @param3: 文字结束计算位置
         * @param4: 是否是从前往后测量
         * @param5: 最大宽度(多久换行)
         * return:一整行有多少个字符
         */
        count = paint.breakText(text, count, text.length, true, width.toFloat(), floats)
        Log.i("szj文字2", "tempCount:$tempCount\tcount:$count")

        // paint.fontSpacing 是系统建议的行与行之间的距离
        canvas.drawText(text,
            tempCount,
            tempCount + count,
            0f,
            -fontMetrics.top + paint.fontSpacing,
            paint)

    }
}