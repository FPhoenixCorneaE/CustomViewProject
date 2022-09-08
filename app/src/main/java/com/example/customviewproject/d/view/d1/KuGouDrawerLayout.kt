package com.example.customviewproject.d.view.d1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AndroidRuntimeException
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import androidx.annotation.IntDef
import androidx.core.view.GestureDetectorCompat
import com.example.customviewproject.R
import com.example.customviewproject.ext.screenWidth

/**
 *
 * @ClassName: KuGouDrawerLayout
 * @Author: 史大拿
 * @CreateDate: 9/7/22$ 7:57 PM$
 * TODO
 */
open class KuGouDrawerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    // 侧滑宽度
    private var drawerWidth = 0

    // 内容缩放比率
    var contentScale = 0f

    // 侧滑最小透明度
    var drawerMinAlpha = 0.0f

    // 设置在drawer模式下，content背景色
    var contentColor = Color.TRANSPARENT

    // 抽屉view
    private lateinit var drawerView: View

    // 内容view
    private lateinit var contentView: View

    // 手势监听
    private val gestureDetector = GestureDetectorCompat(context, DrawerGestureDetector())

    @DrawerStyleMode
    var styleMode = DrawerStyleMode.DEFAULT

    //
    @IntDef(DrawerStyleMode.DEFAULT, DrawerStyleMode.DRAWER)
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.SOURCE)
    annotation class DrawerStyleMode {
        companion object {
            /*
             * 作者:史大拿
             * 创建时间: 9/8/22 1:14 PM
             * 默认模式
             */
            const val DEFAULT = 0

            /*
             * 作者:史大拿
             * 创建时间: 9/8/22 1:14 PM
             * 抽屉样式
             */
            const val DRAWER = 1

        }

    }


    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.KuGouDrawerLayout)
        try {
            styleMode =
                a.getInt(R.styleable.KuGouDrawerLayout_drawer_style, DrawerStyleMode.DEFAULT)
            // 抽屉默认宽度百分比
            val percent =
                a.getFloat(R.styleable.KuGouDrawerLayout_drawer_width_percent, 0.7f)
            drawerWidth = (screenWidth * percent).toInt()
            contentScale = a.getFloat(R.styleable.KuGouDrawerLayout_drawer_content_scale, 0.7f)
            drawerMinAlpha = a.getFloat(R.styleable.KuGouDrawerLayout_drawer_min_alpha, 0.5f)
            contentColor =
                a.getColor(R.styleable.KuGouDrawerLayout_drawer_content_color, Color.TRANSPARENT)
            Log.e("szjdrawerMinAlpha", "$drawerMinAlpha")
        } finally {
            a.recycle()
        }
    }

    // 透明度View
    lateinit var alphaView: View

    // 当xml解析完成回调
    override fun onFinishInflate() {
        super.onFinishInflate()
        // 获取到的是LinearLayout
        val rootContainer = getChildAt(0) as ViewGroup

        if (rootContainer.childCount != 2) {
            throw AndroidRuntimeException("viewGroup布局只能有2个，一个是抽屉布局，一个是内容布局!")
        }
        // TODO 抽屉布局
        drawerView = rootContainer.getChildAt(0)
        // 设置抽屉布局宽度
        val drawerParams = drawerView.layoutParams
        drawerParams.width = drawerWidth
        drawerView.layoutParams = drawerParams

        // TODO 内容布局
        contentView = rootContainer.getChildAt(1)

        if (styleMode == DrawerStyleMode.DRAWER) {
            // TODO 抽屉模式

            val contentParams = contentView.layoutParams
            contentParams.width = screenWidth

            // 1. 删除原布局
            rootContainer.removeView(contentView)

            // 2. 创建一个空的FrameLayout 用于保存原contentView布局和空的View，
            //    空View的作用就是用来改变透明度，从而实现阴影效果
            val tempFrameLayout = FrameLayout(context)
            alphaView = View(context)
            alphaView.setBackgroundColor(contentColor)
            alphaView.alpha = 0f

            // 3. 将原布局添加到frameLayout上
            tempFrameLayout.addView(contentView, contentParams)

            // 4. 将一个空的View添加到frameLayout上用于设置透明度
            tempFrameLayout.addView(alphaView, contentParams)

            tempFrameLayout.layoutParams = contentParams

            // 5.将frameLayout重新添加到viewGroup上
            rootContainer.addView(tempFrameLayout)
        } else {
            // 默认模式
            val contentParams = contentView.layoutParams
            contentParams.width = screenWidth
            contentView.layoutParams = contentParams
        }

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        scrollTo(drawerWidth, 0)
    }

    // 是否拦截 [默认不拦截]
    private var isIntercept = false

    // 是否打开
    private var isOpen = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        isIntercept = false
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // ev.x > drawerWidth  如果当前点击超过了drawerView的宽度 说明是点击的contentView
            // isOpen 是否打开
            // 只有在打开 并且点击contentView的时候，才自己消费事件
            if (ev.x > drawerWidth && isOpen) {
                isIntercept = true
                Log.e("szjInterceptTouchEvent", "X:${ev.x}")

                // 自己消费事件
                return true
            }
        }

        return super.onInterceptTouchEvent(ev)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {

        if (gestureDetector.onTouchEvent(ev)) {
            // 自己消费事件
            return true
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.e("szjTouchEvent", "DOWN")
            }
            MotionEvent.ACTION_UP -> {

                Log.e(
                    "szjTouchEvent",
                    "UP:scrollX:$scrollX\tdrawerWidth:${drawerWidth / 2}isClose:${isIntercept}"
                )

                // 是否在打开drawerView状态下 点击了contentView
                // isClose 是否点击了contentView
                // scrollX == 0 表示drawerView为打开状态
                if (isIntercept && scrollX == 0) {
                    closeDrawer()
                } else {
                    // 如果滑动距离 超过图片的一般就打开
                    // 否则就关闭
                    if (scrollX < drawerWidth / 2) {
                        openDrawer()
                    } else {
                        closeDrawer()
                    }
                }

                return true
            }
        }
        return super.onTouchEvent(ev)
    }

    // 打开
    open fun openDrawer() {
        isOpen = true
        // 打开
        smoothScrollTo(0, 0)
    }

    // 关闭
    open fun closeDrawer() {
        isOpen = false
        // 关闭
        smoothScrollTo(drawerWidth, 0)
    }


    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        Log.e("szjScrollChanged", "styleMode:${styleMode}")

        // 计算偏移百分比
        val ratio = l / drawerWidth.toFloat()
//        Log.e("szjScrollChanged", "ratio:${ratio}")

        if (styleMode == DrawerStyleMode.DRAWER) {
            // 抽屉效果
            contentView.translationX = (l / drawerWidth).toFloat()
            alphaView.alpha = 1 - ratio
        } else {
            // 计算缩放位置
            val contentScale = contentScale + (1 - contentScale) * ratio
            Log.e("szjScrollChanged", "contentScale：${0.2f * ratio}")

            // 设置缩放中心点
            contentView.pivotX = 0f
            contentView.pivotY = contentView.height / 2f
            contentView.scaleX = contentScale
            contentView.scaleY = contentScale


//            val drawerAlpha = 0.5f + (1 - ratio) * 0.5f
            val drawerAlpha = drawerMinAlpha + (1 - ratio) * (1 - drawerMinAlpha)

            // 设置侧滑透明度
            drawerView.alpha = drawerAlpha
            Log.e("szjScrollChanged", "alpha:${drawerAlpha}")
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/8/22 11:17 AM
     * TODO fling监听
     */
    inner class DrawerGestureDetector : GestureDetector.SimpleOnGestureListener() {
        // 滑动事件监听
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (velocityX < 0) { // 打开状态
                closeDrawer()
            } else {
                openDrawer()
            }
            Log.e("szjGestureDetector", "velocityX:${velocityX}\tvelocityY:$velocityX:${isOpen}")
            return true
        }
    }
}