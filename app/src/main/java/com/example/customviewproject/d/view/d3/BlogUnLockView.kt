package com.example.customviewproject.d.view.d3

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AndroidRuntimeException
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.contains
import com.example.customviewproject.ext.distance
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.toast

/**
 *
 * @ClassName: BlogUnLockView
 * @Author: 史大拿
 * @CreateDate: 9/14/22$ 10:39 AM$
 * TODO 九宫格解锁View
 */
open class BlogUnLockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //
        strokeJoin = Paint.Join.BEVEL
    }

    // 大圆半径
    private val bigRadius by lazy { width / (NUMBER * 2) * 0.7f }

    // 小圆半径
    private val smallRadius by lazy { bigRadius * 0.2f }

    // 当前状态
    private var currentType = JiuGonGeUnLockView.Type.ORIGIN

    open var adapter: UnLockBaseAdapter? = null

    companion object {
        private var NUMBER = 3

        // 原始颜色
        private var ORIGIN_COLOR = Color.parseColor("#D8D9D8")

        // 按下颜色
        private var DOWN_COLOR = Color.parseColor("#3AD94E")

        // 抬起颜色
        private var UP_COLOR = Color.parseColor("#57D900")

        // 错误颜色
        private var ERROR_COLOR = Color.parseColor("#D9251E")
    }

    private val unLockPoints = arrayListOf<ArrayList<UnLockBean>>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (adapter == null) {
            throw AndroidRuntimeException("请设置Adapter")
        }
        adapter?.also {
            NUMBER = it.getNumber()
            ORIGIN_COLOR = it.getOriginColor()
            DOWN_COLOR = it.getDownColor()
            UP_COLOR = it.getUpColor()
            ERROR_COLOR = it.getErrorColor()
        }

//        Log.i("measuredWidth", "$measuredWidth")
        val width = resolveSize(measuredWidth, widthMeasureSpec)
        val height = resolveSize(width, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 单个直径
        val diameter = width / NUMBER
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
    }

    // 是否按下
    private var isDOWN = false

    // 记录选中的坐标
    private val recordList = arrayListOf<UnLockBean>()

    private val path = Path()

    private val line = Pair(PointF(), PointF())


    // 密码
    open var password = listOf<Int>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val pointF = isContains(event.x, event.y)
                pointF?.let {
                    // 将当前类型改变为按下类型
                    it.type = JiuGonGeUnLockView.Type.DOWN
                    isDOWN = true

                    recordList.add(it)

                    path.moveTo(it.x, it.y)

                    line.first.x = it.x
                    line.first.y = it.y

                    currentType = JiuGonGeUnLockView.Type.DOWN
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDOWN) {
                    return super.onTouchEvent(event)
                }
                val pointF = isContains(event.x, event.y)
                pointF?.let {
                    // 将当前类型改变为按下类型
                    it.type = JiuGonGeUnLockView.Type.DOWN

                    // 这里会重复调用，所以需要判断是否包含，如果不包含才添加
                    if (!recordList.contains(it)) {
                        recordList.add(it)

                        currentType = JiuGonGeUnLockView.Type.DOWN
                        if (recordList.size >= 2) {
                            // TODO 不穿过圆心
                            val start = recordList[recordList.size - 2]
                            val end = recordList[recordList.size - 1]

                            val d = PointF(start.x, start.y).distance(PointF(end.x, end.y))
                            val dx = (end.x - start.x)
                            val dy = (end.y - start.y)
                            val offsetX = dx * smallRadius / d
                            val offsetY = dy * smallRadius / d

                            val cX = start.x + offsetX
                            val cY = start.y + offsetY
                            path.moveTo(cX, cY)

                            val fX = end.x - offsetX
                            val fY = end.y - offsetY
                            path.lineTo(fX, fY)

                            line.first.x = it.x + offsetX
                            line.first.y = it.y + offsetY

                        }
                    }
                }

                // 手指的位置
                line.second.x = event.x
                line.second.y = event.y
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                // 清空移动线
                line.first.x = 0f
                line.first.y = 0f
                line.second.x = 0f
                line.second.y = 0f


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

                // 密码错误，将标记改变成成错误
                if (!isSuccess) {
                    recordList.forEach {
                        it.type = JiuGonGeUnLockView.Type.ERROR
                    }
                    "输入失败" toast context
                } else {
                    "输入成功" toast context
                }

                // 设置当前状态
                currentType =
                    if (isSuccess) JiuGonGeUnLockView.Type.UP else JiuGonGeUnLockView.Type.ERROR

                // 延迟1秒清空
                postDelayed({
                    clear()
                }, 1000)
            }
        }

        invalidate()
        return true
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/14/22 1:38 PM
     * TODO 用来清空标记
     */
    private fun clear() {
        path.reset() // 重置

        recordList.forEach {
            // 将所有选中状态还原
            it.type = JiuGonGeUnLockView.Type.ORIGIN
        }

        line.first.x = 0f
        line.first.y = 0f
        line.second.x = 0f
        line.second.y = 0f


        recordList.clear()
        isDOWN = false // 标记没有按下

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
//        canvas.drawColor(Color.YELLOW)

//        paint.style = Paint.Style.FILL
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4.dp

        unLockPoints.forEach {
            it.forEach { data ->

                // 根据类型设置颜色
                paint.color = getTypeColor(data.type)

                // 绘制大圆
                paint.alpha = (255 * 0.6).toInt()
                canvas.drawCircle(data.x, data.y, bigRadius, paint)

                // 绘制小圆
                paint.alpha = 255
                canvas.drawCircle(data.x, data.y, smallRadius, paint)
            }
        }

        paint.color = getTypeColor(currentType) // 默认按下颜色
        canvas.drawPath(path, paint)

        // 绘制移动线
        if (line.first.x != 0f && line.second.x != 0f
        ) {
            canvas.drawLine(
                line.first.x,
                line.first.y,
                line.second.x,
                line.second.y,
                paint
            )
        }
    }

    /*
    * 作者:史大拿
    * 创建时间: 9/13/22 1:25 PM
    * TODO 获取类型对应颜色
    */
    private fun getTypeColor(type: JiuGonGeUnLockView.Type): Int {
        return when (type) {
            JiuGonGeUnLockView.Type.ORIGIN -> ORIGIN_COLOR
            JiuGonGeUnLockView.Type.DOWN -> DOWN_COLOR
            JiuGonGeUnLockView.Type.UP -> UP_COLOR
            JiuGonGeUnLockView.Type.ERROR -> ERROR_COLOR
        }
    }
}