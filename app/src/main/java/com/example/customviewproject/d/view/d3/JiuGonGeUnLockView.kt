package com.example.customviewproject.d.view.d3

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AndroidRuntimeException
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.d.view.d3.JiuGonGeUnLockView.Type.*
import com.example.customviewproject.ext.contains
import com.example.customviewproject.ext.distance
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: JiuGonGeUnLockView
 * @Author: 史大拿
 * @CreateDate: 9/13/22$ 9:32 AM$
 * TODO
 */
open class JiuGonGeUnLockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //
        strokeJoin = Paint.Join.BEVEL
    }

    private val unLockPoints = arrayListOf<ArrayList<UnLockBean>>()

    // 大圆半径
    private val bigRadius by lazy { width / (NUMBER * 2) * 0.8f }

    // 小圆半径
    private val smallRadius by lazy { bigRadius * 0.2f }

    // 密码
    open var password = listOf<Int>()

    // 点击回调
    open var resultClick: Click? = null

    // 当前状态
    private var currentType = ORIGIN
    private var currentStyle = Style.FILL

    /// 是否穿过圆心
    private var isDrawLineCenterCircle = false

    open var adapter: UnLockBaseAdapter? = null

    companion object {
        // 设置个数
        private var NUMBER = 3

        // 原始颜色
        private var ORIGIN_COLOR = Color.WHITE

        // 按下颜色
        private var DOWN_COLOR = Color.WHITE

        // 抬起颜色
        private var UP_COLOR = Color.WHITE

        // 错误颜色
        private var ERROR_COLOR = Color.WHITE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (adapter == null) {
            throw AndroidRuntimeException("请设置Adapter")
        }
        adapter?.also {
            NUMBER = it.getNumber()
            currentStyle = it.getStyle()
            ORIGIN_COLOR = it.getOriginColor()
            DOWN_COLOR = it.getDownColor()
            UP_COLOR = it.getUpColor()
            ERROR_COLOR = it.getErrorColor()
            isDrawLineCenterCircle = it.lineCenterCircle()
        }

        Log.i("measuredWidth", "$measuredWidth")
        val width = resolveSize(measuredWidth, widthMeasureSpec)
        val height = resolveSize(width, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        // 单个直径
        val diameter = width / NUMBER

        //
        val ratio = (NUMBER * 2f)
        var index = 1

        // 循环每一行行
        for (i in 0 until NUMBER) {
            val list = arrayListOf<UnLockBean>()

            // 循环每一列
            for (j in 0 until NUMBER) {
                list.add(
                    UnLockBean(
                        width / ratio + diameter * j,
                        height / ratio + diameter * i,
                        index++
                    )
                )
            }
            unLockPoints.add(list)
        }
        Log.i("szjUnLockPoints", "$unLockPoints")

        // TODO 只适合3*3的办法
//        repeat(NUMBER) {
//            repeat(NUMBER) { j ->
//                unLockPoints.add(
//                    arrayListOf(
//                        UnLockBean(width / ratio, height / ratio + diameter * it, index++),
//                        UnLockBean(
//                            width / ratio + diameter,
//                            height / ratio + diameter * it,
//                            index++
//                        ),
//                        UnLockBean(
//                            width / ratio + diameter * 2,
//                            height / ratio + diameter * it,
//                            index++
//                        )
//                    )
//                )
//            }
//        }

        // TODO 笨办法
//        unLockPoints.add(
//            arrayListOf(
//                UnLockBean(width / 6f, height / 6f, 1),
//                UnLockBean(width / 6f + diameter, height / 6f, 2),
//                UnLockBean(width / 6f + diameter * 2, height / 6f, 3)
//            )
//        )
//
//        unLockPoints.add(
//            arrayListOf(
//                UnLockBean(width / 6f, height / 6f + diameter, 4),
//                UnLockBean(width / 6f + diameter, height / 6f + diameter, 5),
//                UnLockBean(width / 6f + diameter * 2, height / 6f + diameter, 6)
//            )
//        )
//
//        unLockPoints.add(
//            arrayListOf(
//                UnLockBean(width / 6f, height / 6f + diameter * 2, 7),
//                UnLockBean(width / 6f + diameter, height / 6f + diameter * 2, 8),
//                UnLockBean(width / 6f + diameter * 2, height / 6f + diameter * 2, 9)
//            )
//        )
    }

    // 记录选中的坐标
    private val recordList = arrayListOf<UnLockBean>()

    // 是否按下
    private var isDOWN = false

    private val path = Path()

    private val line = Pair(PointF(), PointF())

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                clear()

                val pointF = isContains(event.x, event.y)
                pointF?.also {
                    isDOWN = true
                    it.type = DOWN
                    Log.i("szjDOWN", "$it")
                    recordList.add(it)

                    if (isDrawLineCenterCircle) {
                        path.moveTo(it.x, it.y)
                    }

                    line.first.x = it.x
                    line.first.y = it.y
                    currentType = DOWN
                }
            }

            MotionEvent.ACTION_MOVE -> {
//                Log.i("szjMOVE", "移动中")

                if (!isDOWN) {
                    return super.onTouchEvent(event)
                }

                val pointF = isContains(event.x, event.y)
                pointF?.also {
                    it.type = DOWN
                    currentType = DOWN
//                    Log.i("szjDOWN", "$it")
                    if (!recordList.contains(it)) {
                        Log.i("szjDOWN", "${recordList.size}")
                        recordList.add(it)

                        if (recordList.size >= 2) {
                            if (isDrawLineCenterCircle) {
                                // TODO 穿过圆心
                                path.lineTo(it.x, it.y)
                                line.first.x = recordList.last().x
                                line.first.y = recordList.last().y
                            } else {
                                // TODO 不穿过圆心
                                val start = recordList[recordList.size - 2]
                                val end = recordList[recordList.size - 1]

                                val d = PointF(start.x, start.y).distance(PointF(end.x, end.y))
                                val dx = (end.x - start.x)
                                val dy = (end.y - start.y)
                                val offsetX = dx * smallRadius / d
                                val offsetY = dy * smallRadius / d

                                val x1 = start.x + offsetX
                                val y1 = start.y + offsetY
                                path.moveTo(x1, y1)

                                val x2 = end.x - offsetX
                                val y2 = end.y - offsetY
                                path.lineTo(x2, y2)


                                line.first.x = it.x + offsetX
                                line.first.y = it.y + offsetY
                            }
                        }
                    }
                }

                line.second.x = event.x
                line.second.y = event.y

                Log.i("szjDOWN2", "${line.first}\t${line.second}")
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {

                // 抬起时候 无论是否成功，都不绘制最后一段线
                line.first.x = 0f
                line.first.y = 0f

                line.second.x = 0f
                line.second.y = 0f
                if (!isDOWN) {
                    return super.onTouchEvent(event)
                }
                Log.i("szjUP", recordList.toString())


                // 标记是否成功
                val isSuccess =
                    if (recordList.size == password.size) {
                        val list = recordList.zip(password).filter {
                            // 通过判断每一个值
                            it.first.index == it.second
                        }.toList()

                        // 如果每一个值都相同，那么就回调成功
                        list.size == password.size
                    } else {
                        false
                    }

                if (!isSuccess) {
                    recordList.forEach {
                        it.type = ERROR
                    }

                    postDelayed({
                        clear()
                    }, 1000)
                }

                // 设置当前状态
                currentType = if (isSuccess) UP else ERROR

                resultClick?.result(password, recordList.map { it.index }.toList(), isSuccess)
            }
        }

        invalidate()
        return true
    }


    /*
     * 作者:史大拿
     * 创建时间: 9/13/22 10:24 AM
     * TODO 清空所有状态
     */
    open fun clear() {
        unLockPoints.forEach {
            it.forEach { data ->
                data.type = ORIGIN
            }
        }
        recordList.clear()

        isDOWN = false // 默认没有按下
        path.reset() // 重置

        line.first.x = 0f
        line.first.y = 0f

        line.second.x = 0f
        line.second.y = 0f

        invalidate()
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/13/22 10:17 AM
     * TODO 判断是否选中某个圆
     * @param x,y: 点击坐标位置
     */
    private fun isContains(x: Float, y: Float) = let {
        unLockPoints.forEach {
            it.forEach { data ->
                // 循环所有坐标 判断两个位置是否相同
                if (PointF(x, y).contains(PointF(data.x, data.y), bigRadius)) {
                    return@let data
                }
            }
        }
        return@let null
    }

    override fun onDraw(canvas: Canvas) {
        if (currentStyle == Style.FILL) {
            paint.style = Paint.Style.FILL
            paint.strokeWidth = 2.dp
        } else {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2.dp
        }

        unLockPoints.forEach {
            it.forEach { data ->
                // 绘制大圆
                paint.color = getTypeColor(data.type)
                paint.alpha = (255 * 0.6).toInt()
                canvas.drawCircle(data.x, data.y, bigRadius, paint)

                // 绘制小圆
                paint.alpha = 255
                canvas.drawCircle(data.x, data.y, smallRadius, paint)
            }
        }

        // 画线颜色默认为滑动颜色
        paint.color = getTypeColor(currentType)
////        // 画连接线
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4.dp
        canvas.drawPath(path, paint)

//        // 画连接线
        if (line.first.x != 0f && line.second.x != 0f) {
//            paint.style = Paint.Style.STROKE
//            paint.strokeWidth = 10.dp
            canvas.drawLine(line.first.x, line.first.y, line.second.x, line.second.y, paint)
        }
    }


    /*
     * 作者:史大拿
     * 创建时间: 9/13/22 1:25 PM
     * TODO 获取类型对应颜色
     */
    private fun getTypeColor(type: Type): Int {
        return when (type) {
            ORIGIN -> ORIGIN_COLOR
            DOWN -> DOWN_COLOR
            UP -> UP_COLOR
            ERROR -> ERROR_COLOR
        }
    }

    enum class Type {
        ORIGIN, // 原始
        DOWN, // 按下
        UP, // 抬起
        ERROR // 错误
    }

    enum class Style {
        FILL, // 占满
        STROKE, // 空心
    }

    fun interface Click {
        /*
         * 作者:史大拿
         * 创建时间: 9/13/22 2:08 PM
         * TODO
         * @param pwd:正确密码
         * @param inputPuw:输入密码
         * @param isSuccess:是否输入成功
         */
        fun result(pwd: List<Int>, inputPwd: List<Int>, isSuccess: Boolean)
    }
}