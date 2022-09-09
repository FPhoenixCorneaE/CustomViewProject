package com.example.customviewproject.c.view.c2

import android.annotation.SuppressLint
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.*
import androidx.core.animation.doOnEnd
import com.example.customviewproject.ext.getBackgroundBitMap
import com.example.customviewproject.ext.screenHeight
import com.example.customviewproject.ext.screenWidth
import com.example.customviewproject.ext.statusBarHeight
import java.util.*

/**
 *
 * @ClassName: DragBubbleView
 * @Author: 史大拿
 * @CreateDate: 8/18/22$ 4:10 PM$
 * TODO 拖动气泡view
 * 详细实现步骤: https://blog.csdn.net/weixin_44819566/article/details/126441557
 */
class DragBubbleUtil(private val view: View) {

    private val context by lazy { view.context }

    private val windowManager by lazy {
        context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    // 屏幕状态栏高度
    private val statusBarHeight by lazy {
        context.statusBarHeight()
    }

    // 拖动气泡view
    private val dragView by lazy { C2View(context) }

    private val layoutParams by lazy {

        WindowManager.LayoutParams(screenWidth,
            screenHeight,
            TYPE_APPLICATION,
            FLAG_FORCE_NOT_FULLSCREEN,
            PixelFormat.TRANSPARENT // 设置透明度
        )
    }

    // view的图片
    private val bitMap by lazy { view.getBackgroundBitMap() }


    // 需要拖动的View
    @SuppressLint("ClickableViewAccessibility", "Range")
    fun bind() {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
//                    // 和父容器抢焦点
                    view.parent.requestDisallowInterceptTouchEvent(true)

                    // 添加到window上
                    windowManager.addView(dragView, layoutParams)

                    // 当下一帧的时候立马隐藏
                    dragView.postOnAnimation {
                        // 隐藏
                        view.visibility = View.INVISIBLE
                    }


                    // raw绝对位置
                    // getX()是相对于父空间的位置
                    // 设置小圆开始位置

                    // location[0] = x;
                    // location[1] = y;
                    val location = IntArray(2)
                    view.getLocationInWindow(location) // 获取当前窗口的绝对坐标
//                    view.getLocationOnScreen(location) // 获取在整个屏幕内的绝对坐标

                    Log.e("szjLocation", "${location[1] + bitMap.height / 2 - statusBarHeight}")
                    Log.e("szjLocation2", "${screenHeight / 2}")
                    dragView.initPointF(
                        location[0].toFloat() + bitMap.width / 2,
                        location[1].toFloat() + view.height / 2 - statusBarHeight,
                        event.rawX,
                        event.rawY - statusBarHeight)

                    Log.e("szjBitMapDOWN", "bitmapWidth:${bitMap.width}")
                    // 设置图片
                    dragView.upDataBitMap(bitMap, bitMap.width.toFloat())
                }
                MotionEvent.ACTION_MOVE -> {
                    // 如果移动的画，移动大圆位置
                    dragView.upDataPointF(event.rawX, event.rawY - statusBarHeight)
                }
                MotionEvent.ACTION_UP -> {
                    Log.e("szjBitMapUP", "bitmapWidth:${bitMap.width}")
                    if (dragView.isContains()) {
                        // 大圆在小圆内 松手小圆回弹到大圆
                        dragView.bigAnimator().apply {
                            start() // 开启动画

                            doOnEnd { // 动画结束回调

                                Log.e("szjBitMapUP", "end")
                                // 当下一帧的时候立马消失
                                dragView.postOnAnimation {
                                    windowManager.removeView(dragView)
                                    // 更新图片
                                    dragView.upDataBitMap(null, bitMap.width.toFloat())
                                }

                                // 显示
                                view.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        // 清空图片
                        dragView.upDataBitMap(null, bitMap.width.toFloat())
                        // 大圆不在小圆范围内，绘制爆炸效果
                        dragView.explodeAnimator.apply {
                            start() // 开启动画
                            doOnEnd {  // 结束动画回调
                                view.visibility = View.VISIBLE

                                dragView.postOnAnimation {
                                    windowManager.removeView(dragView)
                                    // 显示
                                }
                            }
                        }
                    }
                }
            }
            return@setOnTouchListener true
        }
    }
}