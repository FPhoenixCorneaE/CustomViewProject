package com.example.customviewproject.a.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.szj.szj

/**
 *
 * @ClassName: A1View
 * @Author: 史大拿
 * @CreateDate: 8/5/22$ 9:55 AM$
 * TODO 参考:https://rengwuxian.com/ui-1-1/
 */
class A1View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    // 屏幕宽高
    private val screenWidth = context.szj.screenWidth()
    private val screenHeight = context.szj.screenHeight()

    // 画笔
    private val paint by lazy {
        Paint().also {
            // 设置颜色
            it.color = Color.RED

            // 设置绘制模式
            it.style = Paint.Style.FILL

            // 设置线条宽度
            it.strokeWidth = 3f

            // 抗锯齿
            it.isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /*
         * 作者:史大拿
         * 创建时间: 8/5/22 10:01 AM
         * TODO 设置画布颜色 (红色 黄色)
         * 画布颜色是可以覆盖的，红色 + 黄色 = 绿色，所以会呈现出绿色
         */
//        canvas.drawColor(Color.parseColor("#20ff0000"))
//        canvas.drawColor(Color.parseColor("#2000ff00"))

        // 画圆
        drawCircle(canvas)

        // 画矩形
        drawRect(canvas)

        // 画点
        drawPoint(canvas)

        // 画椭圆
        drawOval(canvas)

        // 画线
        drawLine(canvas)

        // 画圆角矩形
        drawRoundRect(canvas)

        // 画扇形
        drawArc(canvas)

        // 画路径
        drawPath(canvas)
    }

    //region 画路径
    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 1:09 PM
     */
    private fun drawPath(canvas: Canvas) {

        // 1. 画空❤️
        val path = Path()
        path.addArc(RectF(30f, 820f, 30 + 100f, 920f), 140f, 220f) // 添加弧形
        path.arcTo(RectF(30f + 100f, 820f, 130f + 100, 920f), 180f, 220f, false)// 添加第二个弧形
        path.lineTo((130f + 100 + 30) / 2, 920f + 100f) // 添加直线
        path.close() // 闭合

        paint.color = Color.BLACK
        canvas.drawPath(path, paint)

        // 2. 画实❤️
        val path2 = Path()
        path2.addArc(RectF(330f, 820f, 330f + 100f, 920f), 140f, 220f) // 添加弧形
        // @param forceMoveTo: 直线连接到弧形处显示痕迹
        path2.arcTo(RectF(330f + 100f, 820f, 430f + 100, 920f), 180f, 220f, false)// 添加第二个弧形
        path2.lineTo((430f + 100 + 330f) / 2, 920f + 100f) // 添加线
        path2.close() // 闭合

        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas.drawPath(path2, paint)

        // Path.Direction.CCW: clockwise 顺时针
        // Path.Direction.CW: counter-clockwise: 逆时针
        // 3. 画圆
        val path3 = Path()
        path3.addCircle(120f, 1150f, 100f, Path.Direction.CCW)
        path3.addCircle(120f + 100f, 1150f, 100f, Path.Direction.CCW)
        paint.style = Paint.Style.STROKE // 空心
        canvas.drawPath(path3, paint)


        // 4. 画圆
        val path4 = Path()
        path4.addCircle(450f, 1150f, 100f, Path.Direction.CW)
        path4.addCircle(450f + 100f, 1150f, 100f, Path.Direction.CW)
        paint.style = Paint.Style.FILL // 实心
        canvas.drawPath(path4, paint)


        // 5. 画圆
        val path5 = Path()
        path5.addCircle(780f, 1150f, 100f, Path.Direction.CCW)
        path5.addCircle(780f + 100f, 1150f, 100f, Path.Direction.CW)
        paint.style = Paint.Style.FILL
//        EVEN_ODD [交叉填充]
//        WINDING [全填充]（默认值）
//        INVERSE_EVEN_ODD
//        INVERSE_WINDING
        path.fillType = Path.FillType.EVEN_ODD
        canvas.drawPath(path5, paint)

        // 6. 画线
        val path6 = Path()
        path6.moveTo(50f, 1320f)
        path6.lineTo(90f, 1340f)
        path6.lineTo(140f, 1290f)
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path6, paint)


        // 7. 画线
        val path7 = Path()
        path7.moveTo(150f, 1320f)
        path7.lineTo(350f, 1320f)
        path7.arcTo(RectF(250f, 1350f, 350f, 1450f), 0f, 350f, false) // forceMoveTo 连接点有痕迹
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path7, paint)
    }
    //endregion

    //region 画扇形
    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 11:14 AM
     */
    private fun drawArc(canvas: Canvas) {

        // 1.
        paint.color = Color.RED
        canvas.drawRect(RectF(20f, 700f, 100 + 20f, 800f), paint)
        /*
         * 作者:史大拿
         * 创建时间: 8/5/22 1:01 PM
         * @param starAngle: 开始角度
         * @param sweepAngle: 扫描角度
         * @param useCenter: 空心状态下，是否绘制封口弧形 [true绘制]
         */
        paint.color = Color.GREEN
        canvas.drawArc(20f, 700f, 100f + 20f, 800f, 0f, 90f, true, paint)


        // 2.
        paint.color = Color.RED
        canvas.drawRect(RectF(150f, 700f, 150f + 100f, 800f), paint)

        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE // 空心模式
        canvas.drawArc(150f, 700f, 150f + 100f, 800f, 0f, 180f, false, paint)


        // 3.
        paint.color = Color.RED
        canvas.drawRect(RectF(150f + 100f + 20f, 700f, 270f + 100f, 800f), paint)

        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE // 空心模式
        canvas.drawArc(270f, 700f, 270f + 100f, 800f, 0f, 180f, true, paint)
    }


    //endregion

    //region 画圆角矩形
    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 11:08 AM
     */
    private fun drawRoundRect(canvas: Canvas) {
        // 1.
        /*
         * 作者:史大拿
         * 创建时间: 8/5/22 11:11 AM
         * @param rx: 圆角的横向半径
         * @param ry: 圆角的纵向半径
         */
        paint.color = Color.RED
        canvas.drawRoundRect(20f, 550F, screenWidth / 3f, 550f + 100f, 40f, 40f, paint)


        // 2.
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(screenWidth / 3f + 20f,
            550F,
            screenWidth / 3f * 2,
            550f + 100f,
            40f,
            40f,
            paint)

    }


    //endregion

    //region 画线
    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 10:53 AM
     */
    @SuppressLint("Range")
    private fun drawLine(canvas: Canvas) {
        // 1.
        paint.strokeWidth = 6f
        paint.color = Color.RED
        canvas.drawLine(30f, 460f, screenWidth / 2f - 30f, 500f, paint)
        canvas.drawLine(30f, 500f, screenWidth / 2f - 30f, 460f, paint)

        // 2. 绘制多条线
        var i = 0
        val floats = floatArrayOf(
            screenWidth / 2f + i * 30, // startX,
            460f, // endX
            screenWidth / 2f + i * 30, // endX
            520f,    // endY

            screenWidth / 2f + ++i * 30,
            460f,
            screenWidth / 2f + i * 30,
            520f,

            screenWidth / 2f + ++i * 30,
            460f,
            screenWidth / 2f + i * 30,
            520f,

            screenWidth / 2f + ++i * 30,
            460f,
            screenWidth / 2f + i * 30,
            520f
        )
        paint.color = Color.GREEN
        canvas.drawLines(floats, paint)

    }
    //endregion

    //region  画椭圆
    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 10:47 AM
     */
    private fun drawOval(canvas: Canvas) {
        // 1.
        canvas.drawOval(10f, 350f, screenWidth / 4f, 420f, paint)

        // 2.
        paint.style = Paint.Style.STROKE // 空心椭圆
        canvas.drawOval(screenWidth / 4 + 10f, 350f, screenWidth / 4 * 2f, 420f, paint)

        // 3.
        paint.color = Color.GREEN
        paint.strokeWidth = 2f
        canvas.drawOval(RectF(screenWidth / 4 * 2 + 20f, 350f, screenWidth / 4 * 3f, 420f),
            paint)
    }

    //endregion

    //region 画点
    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 10:28 AM
     */
    @SuppressLint("Range")
    private fun drawPoint(canvas: Canvas) {
        // 1.
        paint.color = Color.RED
        paint.strokeCap = Paint.Cap.SQUARE // 小正方形 (默认)
        canvas.drawPoint(20f, 300f, paint)

        // 2.
        paint.color = Color.BLACK
        paint.strokeCap = Paint.Cap.ROUND // 小圆点
        canvas.drawPoint(20 * 2f, 300f, paint)

        var i = 2
        // 3.设置多个点
        paint.color = Color.GREEN
        val floats = floatArrayOf(
            20f * ++i, 300f, // x1 ,y1
            20f * ++i, 300f, // x2 ,y2
            20f * ++i, 300f, // x3 ,y3
            20f * ++i, 300f, // x4 ,y4
            20f * ++i, 300f, // x5 ,y5
        )
        canvas.drawPoints(floats, paint)


        paint.color = Color.RED
        val floats2 = floatArrayOf(
            20f * ++i, 300f, // x1 ,y1
            20f * ++i, 300f, // x2 ,y2
            20f * ++i, 300f, // x3 ,y3
            20f * ++i, 300f, // x4 ,y4
            20f * ++i, 300f, // x5 ,y5
            20f * ++i, 300f, // x6 ,y6
            20f * ++i, 300f, // x7 ,y7
            20f * ++i, 300f, // x8 ,y8
            20f * ++i, 300f, // x9 ,y9
            20f * ++i, 300f, // x10 ,y10
        )
        /*
         * 作者:史大拿
         * 创建时间: 8/5/22 10:44 AM
         * @param floats2: 需要展示的数据，一共20个数据，展示10对点
         * @param offset: 省略前4个点【省略前2个点】
         * @param count: 一共又多少个点 【从省略处开始数往后展示多少个点，17 / 2 = 7，所以往后展示7个点】
         * @param paint: 画笔
         */
        canvas.drawPoints(floats2, 4, 14, paint)
    }
    //endregion

    //region 画矩形
    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 10:10 AM
     */
    private fun drawRect(canvas: Canvas) {
        // 1.
        paint.style = Paint.Style.STROKE // 空心样式
        paint.strokeWidth = 10f // 设置线条宽度
        canvas.drawRect(10f, 150f, screenWidth / 8f, 150f + 100f, paint)

        // 2.
        paint.style = Paint.Style.FILL // 实心样式
        canvas.drawRect(screenWidth / 8 + 10f,
            150f,
            screenWidth / 8 * 2f,
            150f + 100f,
            this.paint)


        // 3.
        paint.color = Color.BLACK
        canvas.drawRect(Rect(screenWidth / 8 * 2 + 10, 150, screenWidth / 8 * 3, 150 + 100),
            paint)


        // 3.
        paint.color = Color.GREEN
        canvas.drawRect(RectF(screenWidth / 8 * 3 + 10f, 150f, screenWidth / 8 * 4f, 150 + 100f),
            paint)

    }
    //endregion

    //region 画圆
    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 10:04 AM
     */
    private fun drawCircle(canvas: Canvas) {

        // 1.
        paint.style = Paint.Style.STROKE // 空心样式
        canvas.drawCircle(60f, 60f, 50f, paint)

        // 2.
        paint.style = Paint.Style.FILL // 填充样式
        canvas.drawCircle(180f, 60f, 50f, paint)

        // 3.
        paint.style = Paint.Style.FILL_AND_STROKE // 即填充 又 铺满
        canvas.drawCircle(300f, 60f, 50f, paint)

        // 4.
        paint.style = Paint.Style.STROKE // 空心样式
        paint.strokeWidth = 15f // 设置线条宽度
        canvas.drawCircle(480f, 60f, 50f, paint)
    }
    //endregion

}