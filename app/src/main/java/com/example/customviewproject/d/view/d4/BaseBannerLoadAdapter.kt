package com.example.customviewproject.d.view.d4

import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @ClassName: BaseBannerLoadAdapter
 * @Author: 史大拿
 * @CreateDate: 10/28/22$ 11:12 AM$
 * TODO
 */
abstract class BaseBannerLoadAdapter {

    /*
     * 作者:史大拿
     * 创建时间: 10/28/22 11:13 AM
     * TODO 最后一页关闭时间
     */
    fun closeTime() = 300L


    /*
     * 作者:史大拿
     * 创建时间: 10/28/22 11:14 AM
     * TODO viewpager通过fragment，所以需要 fragmentManager
     */
    abstract fun getAdapter(): RecyclerView.Adapter<*>

    /*
     * 作者:史大拿
     * 创建时间: 10/28/22 11:15 AM
     * TODO 每个 fragment 布局 id
     */
    @IdRes
    abstract fun getListLayoutId(): List<Int>

    /*
     * 作者:史大拿
     * 创建时间: 10/28/22 11:15 AM
     * TODO 加载更多页面id
     */
    @LayoutRes
    abstract fun loadId(): Int

    /*
     * 作者:史大拿
     * 创建时间: 10/28/22 11:16 AM
     * TODO 加载更多宽度 = 屏幕宽度 * loadWidth
     */
    @FloatRange(from = 0.0, to = 1.0)
    fun loadWidth() = 0.2


    interface Click {
        /*
         * 作者:史大拿
         * 创建时间: 10/28/22 2:53 PM
         * TODO 加载的view
         * tips: 需要在 BannerLoadView#setAdapter()前调用
         */
        fun loadView(view: View)

        // ViewPager2中的方法
        fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
        fun onPageSelected(position: Int)
        fun onPageScrollStateChanged(state: Int)

        /*
         * 作者:史大拿
         * 创建时间: 10/28/22 2:49 PM
         * TODO 当滑动到加载更多时候执行
         */
        fun loadMore()
    }

    var click: Click? = null

}