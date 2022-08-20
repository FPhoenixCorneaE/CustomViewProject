package com.example.customviewproject.c.view.c2.blog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.animation.doOnEnd
import com.example.customviewproject.ext.getBackgroundBitMap
import com.example.customviewproject.ext.screenHeight
import com.example.customviewproject.ext.screenWidth
import com.example.customviewproject.ext.statusBarHeight

/**
 *
 * @ClassName: BlogDragBubbleUtil
 * @Author: 史大拿
 * @CreateDate: 8/20/22$ 1:56 PM$
 * TODO
 */
class BlogDragBubbleUtil(private val view: View) {
    private val context by lazy { view.context }

    // 气泡View
    private val dragView by lazy { DragView(context) }

    // 初始化windowManager
    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    // layoutParams
    private val layoutParams by lazy {
//        WindowManager.LayoutParams().apply {
//            format = PixelFormat.TRANSLUCENT // 设置windowManager为透明
//        }
        WindowManager.LayoutParams(screenWidth,
            screenHeight,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
            PixelFormat.TRANSPARENT // 设置透明度
        )
    }

    // 屏幕状态栏高度
    private val statusBarHeight by lazy {
        context.statusBarHeight()
    }

    // view的图片
    private val bitMap by lazy { view.getBackgroundBitMap() }


    @SuppressLint("ClickableViewAccessibility")
    fun bind() {
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 和父容器抢焦点
                    view.parent.requestDisallowInterceptTouchEvent(true)


                    // 添加
                    windowManager.addView(dragView, layoutParams)

                    dragView.postOnAnimation {
                        // 隐藏View
                        view.visibility = View.INVISIBLE
                    }
                    val location = IntArray(2)
                    view.getLocationInWindow(location) // 获取当前窗口的绝对坐标
                    dragView.initPointF( // 初始化位置
                        location[0].toFloat() + view.width / 2,
                        location[1].toFloat() + view.height / 2 - statusBarHeight,
                        event.rawX,
                        event.rawY - statusBarHeight
                    )

                    Log.e("szjBitMap1",
                        "x:${location[0]}\ty:${location[1]}statusBarHeight:${statusBarHeight}\tviewHeight:${view.height / 2}")
                    Log.e("szjBitMap2", "width:${bitMap.width}\theight:${bitMap.height}")
                    // 设置BitMap图片
                    dragView.upDataBitMap(bitMap, bitMap.width.toFloat())

                }
                MotionEvent.ACTION_MOVE -> {
//                    dragView.upDataBitMap(bitMap, bitMap.width.toFloat())
                    // 重新绘制大圆位置
                    dragView.upDataPointF(event.rawX, event.rawY - statusBarHeight)

                }
                MotionEvent.ACTION_UP -> {

                    /// 判断大圆是否在辅助圆内
                    if (dragView.isContains()) {
                        // 回弹效果
                        dragView.bigAnimator().run {
                            start()
                            doOnEnd { // 结束回调
                                // 在下一帧的时候在删除drawView
                                dragView.postOnAnimation {
                                    // 删除
                                    windowManager.removeView(dragView)
                                    dragView.upDataBitMap(null, bitMap.width.toFloat())
                                }
                                // 显示View
                                view.visibility = View.VISIBLE
                            }
                        }

                    } else {
                        // 爆炸效果

                        // 爆炸之前先清空ViewBitMap
                        dragView.upDataBitMap(null, bitMap.width.toFloat())
                        dragView.explodeAnimator.run {
                            start() // 开启动画
                            doOnEnd {  // 结束动画回调
                                view.visibility = View.VISIBLE
                                dragView.postOnAnimation {
                                    windowManager.removeView(dragView)
                                }
                            }
                        }
                    }
                }
            }
            true
        }
    }
}