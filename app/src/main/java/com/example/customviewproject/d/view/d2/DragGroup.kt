package com.example.customviewproject.d.view.d2

import android.annotation.SuppressLint
import android.content.Context
import android.util.AndroidRuntimeException
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import androidx.core.view.ViewCompat
import androidx.core.widget.ListViewCompat
import androidx.customview.widget.ViewDragHelper
import com.example.customviewproject.R

/**
 *
 * @ClassName: DragGroup
 * @Author: 史大拿
 * @CreateDate: 9/9/22$ 1:51 PM$
 * TODO
 */
open class DragGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val TAG = "DragGroup"
    }

    // 第一个view
    private lateinit var firstView: View

    // 第二个view
    private lateinit var secondView: View

    // 是否打开
    private var isOpen = false

    // 第一个view是否消费事件
    private var isFirstViewConsumptionEvent = false

    // 点击时候默认是否关闭
    private var isClickClose = false

    private val viewDrag by lazy {
        ViewDragHelper.create(this, CustomDragHelper())
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DragGroup)
        try {
            isClickClose = a.getBoolean(R.styleable.DragGroup_drag_is_close, false)
        } finally {
            a.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 2) {
            throw AndroidRuntimeException("只能够存在2个view")
        }
        firstView = getChildAt(0)
        secondView = getChildAt(1)
    }

    private var tempDownY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.i(TAG, "onInterceptTouchEvent:${ev.action}")
//        isFirstViewConsumptionEvent = false
        when (ev.action) {

            MotionEvent.ACTION_DOWN -> {
                tempDownY = ev.y

                // 如果当前点击的位置 在第一个view内，那么就让第一个view自己处理事件
                Log.i(
                    TAG,
                    "szjMOVE:Y:${ev.y}\tfirstHeight:${firstView.height}\tsecondTop:${secondView.top}"
                )
                if (ev.y <= firstView.height && secondView.top > 0) {
                    isOpen = false // 如果自己消费了事件，默认会
                    isFirstViewConsumptionEvent = true
                    return super.onInterceptTouchEvent(ev)
                }

                // 给viewDragHelper DOWN事件
                // 否则会提示这个错误:Ignoring pointerId=0 because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.
                // 意思为MOVE事件没有DOWN事件
                viewDrag.processTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {


                // ev.y - tempDownY > 0 : 是否向上滑动
                // canChildScrollUp() 是否滑动到顶部
                // 如果可以向上滑动 并且没有滑动到顶部 那么就自己消费事件
                if (ev.y - tempDownY > 0 && !canChildScrollUp()) {
                    Log.i(TAG, "onInterceptTouchEvent: Y:${ev.y}\ttempY:${tempDownY}")
                    return true
                }

                // 如果是打开状态，那么也自己消费事件
                if (isOpen) {
                    return true
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.i(TAG, "onLayout:changed:${changed}")

        /*
         * 作者:史大拿
         * 创建时间: 9/9/22 4:31 PM
         * @param isFirstViewConsumptionEvent:firstView消费了事件
         * @param isClickClose: 点击firstView时候，是否允许关闭
         */
        if (isFirstViewConsumptionEvent && !isClickClose) {
            secondView.top = firstView.height
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/9/22 3:00 PM
     * TODO 参考自SwipeRefreshLayout#canChildScrollUp() 判断view是否到达顶部
     */
    open fun canChildScrollUp(): Boolean {
        return if (secondView is ListView) {
            ListViewCompat.canScrollList((secondView as ListView?)!!, -1)
        } else secondView.canScrollVertically(-1)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
//        if (event.action == MotionEvent.ACTION_UP) {
//            viewDrag.processTouchEvent(event)
//            return super.onTouchEvent(event)
//        } else {
//
//        }

        Log.i(TAG, "onTouchEvent:${event.action}")

        viewDrag.processTouchEvent(event)

        // 如果是抬起状态 并且是打开状态
//        if (event.action == MotionEvent.ACTION_UP && isOpen) {
//            open()
//        }
        return true

    }


    inner class CustomDragHelper : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return secondView == child
        }

        // 上下滑动
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            Log.i(TAG, "clampViewPositionVertical:top:${top}\theight:${firstView.height}")

            return when {
                // 如果当前滑动距离 > 第一个view的高度
                top > firstView.height -> {
                    // 那就不让他滑动了
                    firstView.height
                }
                top < 0 -> {
                    // 如果top < 0 那么也不让他滑动了
                    0
                }
                else -> {
                    // 反之则正常滑动
                    top
                }
            }
        }

        // 左右滑动
//        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
////            return left
//            return super.clampViewPositionHorizontal(child, left, dx)
//        }


        // 滑动结束回调
        /*
         * 作者:史大拿
         * 创建时间: 9/9/22 2:02 PM
         * @param childView:当前滑动的view
         * @param xVel:x方向滑动速度
         * @param yVel:y方向滑动速度
         */
        override fun onViewReleased(childView: View, xVel: Float, yVel: Float) {
            Log.i(TAG, "childView:${childView.top}\t y:${firstView.height}\t:yVel:$yVel")

            // 如果当前滑动位置 > child高度的一半
            // 那么就让控件占满
            if (childView.top > firstView.height / 2 || yVel > 0) {
                open()
            } else {
                close()
            }

            invalidate()
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            // 结束回调
            // 并且第一个view消费了事件 【第一个view消费了事件会重新绘制，导致关闭,】
            Log.i(TAG, "onViewDragStateChanged:${state}---11")
            if (ViewDragHelper.STATE_DRAGGING == state && isFirstViewConsumptionEvent) {
//                open()
                Log.i(TAG, "onViewDragStateChanged:${state}----22")
            }
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/9/22 4:28 PM
     * 这里可能会报错
     *  报错原因: viewDrag.processTouchEvent(ev) 没有消费事件，所以在移动过程中 可能会提示 Cannot settleCapturedViewAt outside of a call toCallback#onViewReleased
     */
    private fun open() {
        isOpen = true
        viewDrag.settleCapturedViewAt(0, firstView.height)
    }

    private fun close() {
        isOpen = false
        viewDrag.settleCapturedViewAt(0, 0)
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/9/22 2:18 PM
     * 使用ViewDragHelper#settleCapturedViewAt() 滑动必须添加!
     */
    override fun computeScroll() {
        if (viewDrag.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }
}