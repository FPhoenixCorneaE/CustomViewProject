package com.example.customviewproject.d.view.d4

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING
import androidx.viewpager.widget.ViewPager
import com.example.customviewproject.ext.screenWidth
import kotlin.math.abs

/**
 *
 * @ClassName: BannerLoadView
 * @Author: 史大拿
 * @CreateDate: 10/27/22$ 11:14 AM$
 * TODO
 */
open class BannerLoadView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {

    companion object {
        const val TAG = "szjBannerLoadView"

    }

    private val viewPager by lazy {
        BannerLoadViewPager(context, attrs).apply {
            id = generateViewId()
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 10/28/22 9:44 AM
     * TODO 第二个view的宽
     */
    private var secondWidth = 0

    /*
     * 作者:史大拿
     * 创建时间: 10/27/22 3:05 PM
     * TODO 当前viewPager下标
     */
    private var viewPagerPosition = 0

    private lateinit var adapter: BannerLoadViewPagerAdapter

    override fun onFinishInflate() {
        super.onFinishInflate()
        orientation = HORIZONTAL
        addView(
            viewPager,
            LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.MATCH_PARENT)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        secondWidth = getChildAt(1).width
    }

    private var loadAdapter: BaseBannerLoadAdapter? = null

    open fun getAdapter(): BaseBannerLoadAdapter {
        if (loadAdapter == null) {
            throw RuntimeException("loadAdapter == null?")
        }
        return loadAdapter!!
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setAdapter(
        loadAdapter: BaseBannerLoadAdapter
    ) {
        this.loadAdapter = loadAdapter
        val fragments = loadAdapter.getListLayoutId().map {
            BannerLoadFragment.getInstance(it)
        }.toList()

        adapter = BannerLoadViewPagerAdapter(getAdapter().getFragmentManager(), fragments)
        viewPager.adapter = adapter

        // 监听viewpager滑动
        viewPager.addOnPageChangeListener(this)

        val view = LayoutInflater.from(context).inflate(getAdapter().loadId(), null, false)

        getAdapter().click?.loadView(view)

        // 添加更多的数据
        addView(
            view,
            LayoutParams(
                (screenWidth * getAdapter().loadWidth()).toInt(),
                MarginLayoutParams.MATCH_PARENT
            )
        )
    }


    // 最后一页是否向左滑动
    private var isLastPageSwipeLeft = false

    private var isTouchLeft = false
    private var mLastDownX = 0f
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e("szj事件分发", "dispatchTouchEvent:${ev.action}")
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastDownX = ev.x
            }
            MotionEvent.ACTION_MOVE -> {
                isTouchLeft = mLastDownX - ev.x > 0 //判断滑动方向
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.e("szj事件分发", "onInterceptTouchEvent:${ev.action}")
        Log.e(
            TAG,
            "onInterceptTouchEvent:isSwipeLeft:${isLastPageSwipeLeft}\t:${ev.action}\t:isTouchLeft:${isTouchLeft}"
        )


        if (isLastPage() && isLastPageSwipeLeft) {
            return true
        }

        return super.onInterceptTouchEvent(ev)
    }

    private var downX = 0f
    open var offsetX = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        Log.e("szj事件分发", "onTouchEvent:${event.action}")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e(TAG, "MOVE:offset${offsetX}\tsecondWidth:${secondWidth}")
                offsetX = event.x - downX

                if (abs(offsetX) >= secondWidth.toFloat()) {
                    offsetX = -secondWidth.toFloat()
                }

                if (offsetX > 0f) {
                    offsetX = 0f
                }

                scrollTo(-offsetX.toInt(), 0)

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (offsetX < 0 && isLastPage()) {
                    closeAnimator()
                }

                viewPager.setSlide(false)
            }
        }
        return true
    }

    private fun closeAnimator() {
        val animator = ObjectAnimator.ofFloat(this, "offsetX", offsetX, 0f)
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            offsetX = value
            scrollTo((-offsetX).toInt(), 0)
        }

        // 动画结束回调
        animator.doOnEnd {
            offsetX = 0f
            isLastPageSwipeLeft = false
//            getAdapter().click?.loadMore()
        }
        animator.duration = getAdapter().closeTime()
        animator.start()
    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        getAdapter().click?.onPageScrolled(position, positionOffset, positionOffsetPixels)
        Log.e(
            TAG,
            "onPageScrolled11:p:$position\tcurrentState:${currentState}"
        )
    }

    /*
     * 作者:史大拿
     * 创建时间: 10/27/22 3:14 PM
     * TODO 当页面切换完成时候回调
     */
    override fun onPageSelected(position: Int) {
        getAdapter().click?.onPageSelected(position)
        viewPagerPosition = position
        Log.e(
            TAG,
            "onPageScrolled22:$position"
        )
    }

    /*
     * 作者:史大拿
     * 创建时间: 10/28/22 10:00 AM
     * TODO 是否是最后一页
     */
    private fun isLastPage(): Boolean {
        Log.e(TAG, "viewPagerPosition:${viewPagerPosition}\tcount:${adapter.count - 1}")
        return viewPagerPosition == adapter.count - 1
    }


    /*
     * 作者:史大拿
     * 创建时间: 10/28/22 10:11 AM
     * TODO 当前viewPager滑动状态
     */
    private var currentState = 0

    /*
     * 作者:史大拿
     * 创建时间: 10/27/22 3:14 PM
     * TODO 当页面状态发生变化时候回调
     */
    override fun onPageScrollStateChanged(state: Int) {
        getAdapter().click?.onPageScrollStateChanged(state)
        currentState = state
        when (state) {
            SCROLL_STATE_IDLE -> {

                Log.e(TAG, "onPageScrollStateChanged:页面空闲中..")
            }
            SCROLL_STATE_DRAGGING -> {
                // 如果是最后一页 并且向左滑动 并且 正处于空闲状态
                if (isLastPage() && isTouchLeft) {
                    isLastPageSwipeLeft = true
                    viewPager.setSlide(true)
                }
                Log.e(TAG, "onPageScrollStateChanged:拖动中..")
            }
            SCROLL_STATE_SETTLING -> {
                Log.e(TAG, "onPageScrollStateChanged:拖动停止了..")
            }
        }
        Log.e(
            "onPageScrolled33",
            "state:${state}\tcurrentItem:${viewPager.currentItem}\tisLastPageSwipeLeft:${isLastPageSwipeLeft}"
        )
    }


}