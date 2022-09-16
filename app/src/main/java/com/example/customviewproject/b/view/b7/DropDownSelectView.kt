package com.example.customviewproject.b.view.b7

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AndroidRuntimeException
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd

/**
 *
 * @ClassName: DropDownSelectView
 * @Author: 史大拿
 * @CreateDate: 9/15/22$ 2:37 PM$
 * TODO
 */
open class DropDownSelectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {

        // 阴影颜色
        private val SHADOW_COLOR = Color.parseColor("#88888888")

        // 动画展示时间
        const val CONTENT_DURATION = 350L

        private const val TAG = "szjDropDownSelectView"

        // 内容高度百分比 [1占满全屏]
        const val CONTENT_VIEW_HEIGHT = 0.75f
    }

    // 顶部tabView
    private lateinit var tabView: LinearLayout

    // 内容
    private lateinit var contentView: LinearLayout

    // 阴影
    private lateinit var shadowView: View

    // 内容高度
    private var contentHeight = 0f

    // 标记是否是打开状态
    private var isOpen = false

    inner class AdapterObserved : DropDownObserved() {
        override fun closeContent() {
            close()
        }
    }

    private var adapterObserved: AdapterObserved? = null

    open var adapter: BaseDropDownAdapter? = null
        set(value) {


            if (adapterObserved != null) {
                value?.unregister(adapterObserved!!)
            }

            field = value
            if (field == null) {
                throw AndroidRuntimeException("adapter null?")
            }

            // 注册
            adapterObserved = AdapterObserved()
            value?.register(adapterObserved!!)



            tabView.orientation = HORIZONTAL
            field?.let { adapter ->
                for (position in 0 until adapter.count()) {

                    // TODO 添加tableView
                    val tView = adapter.tabView(position, tabView)
                    val tvParams = LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    tvParams.weight = 1f
                    tView.layoutParams = tvParams
                    tabView.addView(tView)
                    // tableView点击事件
                    tableOnClick(tView, position)

                    // TODO 添加contentView
                    val cView = adapter.contentView(position, contentView)
                    cView.visibility = View.GONE
                    contentView.addView(cView)
                }
            }

        }

    private var currentPosition = -1

    private fun tableOnClick(view: View, position: Int) {
        view.setOnClickListener {
            // 事件回调
            adapter?.blockClick?.click(
                view,
                tabView.getChildAt(currentPosition),
                position,
                currentPosition
            )

            // 已经打开状态，点击其他菜单
//            "切换了${position}" toast context
            Log.e("szjClick", "currentPosition:${currentPosition}\tposition:$position")
            when (currentPosition) {
                -1 -> {
                    // 没有打开状态，打开菜单 ，只有关闭了才能打开
                    if (!isOpen) {
                        open(position)
                    }
                }
                position -> {
                    // 点击同一个position 表示关闭， 只有打开了才能关闭
                    if (isOpen) {
                        close()
                    }
                }
                else -> {
                    /// 显示现在的
                    contentView.getChildAt(position).visibility = View.VISIBLE

                    // 隐藏上一个点击
                    contentView.getChildAt(currentPosition).visibility = View.GONE

                    currentPosition = position
                }
            }
        }
    }

    // 阴影透明度
    private val shadowAlphaShowAnimator by lazy {
        val animator =
            ObjectAnimator.ofFloat(shadowView, "alpha", 0f, 1f)
        animator.duration = CONTENT_DURATION
        animator
    }

    private val shadowAlphaHideAnimator by lazy {
        val animator =
            ObjectAnimator.ofFloat(shadowView, "alpha", 1f, 0f)
        animator.duration = CONTENT_DURATION
        animator
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/15/22 4:11 PM
     * TODO 打开
     */
    open fun open(position: Int) {
        isOpen = true
        val animator =
            ObjectAnimator.ofFloat(contentView, "translationY", -contentHeight, 0f)
        animator.duration = CONTENT_DURATION
        animator.start()

        // 阴影动画开启
        shadowAlphaShowAnimator.start()
        // 显示阴影
        shadowView.visibility = View.VISIBLE

        // 阴影显示完成之后在显示内容
        currentPosition = position

        contentView.getChildAt(currentPosition).visibility = View.VISIBLE

        // 打开回调
        adapter?.blockClick?.openView(tabView.getChildAt(currentPosition), currentPosition)
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/15/22 4:11 PM
     * TODO 关闭
     */
    open fun close() {
        isOpen = false
        val animator =
            ObjectAnimator.ofFloat(contentView, "translationY", 0f, -contentHeight)
        animator.duration = CONTENT_DURATION
        animator.start()
        animator.doOnEnd {
            shadowView.visibility = View.GONE

            Log.e("szjDoOnEnd", "$currentPosition")
            // 吧当前选中contentView的隐藏掉
            contentView.getChildAt(currentPosition).visibility = View.GONE

            // 关闭回调
            adapter?.blockClick?.closeView(tabView.getChildAt(currentPosition), currentPosition)

            currentPosition = -1
        }

        shadowAlphaHideAnimator.start()
    }

    init {
        // 设置垂直布局
        orientation = VERTICAL

        initHeadView()

        initFrameLayout()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(TAG, "onMeasure")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.i(TAG, "onSizeChanged:width:$w\theight:$h\theight:${height}")
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.e(TAG, "onLayout")
        if (contentHeight == 0f) {
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.height = (height * CONTENT_VIEW_HEIGHT).toInt()
            contentHeight = layoutParams.height.toFloat()
            contentView.layoutParams = layoutParams

            contentView.translationY = -contentHeight
        }

    }


    private fun initFrameLayout() {
        val containerView = FrameLayout(context)

        // 创建内容group
        contentView = LinearLayout(context)
        contentView.setBackgroundColor(Color.WHITE)

        // 创建内容group背景色
        shadowView = View(context)
        shadowView.setBackgroundColor(SHADOW_COLOR)
        shadowView.setOnClickListener {
            // 如果是打开状态才可以关闭
            if (isOpen) {
                close() // 点击阴影关闭弹窗
            }
        }
        shadowView.visibility = View.GONE

        // 将内容group 和 背景色添加到容器中
        containerView.addView(shadowView)
        containerView.addView(contentView)

        // 将外层容器添加到linear上
        addView(containerView)

    }

    @SuppressLint("SetTextI18n")
    fun initHeadView() {
        tabView = LinearLayout(context)
//        tabView.setBackgroundColor(Color.RED)
        val headParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tabView.layoutParams = headParams

//        // TODO 初始化TextView
        addView(tabView)
    }
}