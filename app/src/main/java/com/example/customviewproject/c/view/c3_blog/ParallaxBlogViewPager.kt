package com.example.customviewproject.c.view.c3_blog

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.customviewproject.c.fragment.C3BlogFragment
import com.example.customviewproject.c.fragment.C3Fragment
import com.example.customviewproject.c.view.c3.C3Bean
import java.util.ArrayList

/**
 *
 * @ClassName: ParallaxBlogViewPager
 * @Author: 史大拿
 * @CreateDate: 8/31/22$ 10:26 AM$
 * TODO
 */
class ParallaxBlogViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    fun setLayout(fragmentManager: FragmentManager,
                  @LayoutRes list: ArrayList<Int>,
                  block: (Int, Int, Fragment) -> Unit,
                  ) {
        val listFragment = arrayListOf<C3BlogFragment>()
        // 加载fragment
        list.map {
            C3BlogFragment.instance(it)
        }.forEach {
            listFragment.add(it)
        }

        adapter = ParallaxBlockAdapter(listFragment, fragmentManager)

        // 监听变化
        addOnPageChangeListener(object : OnPageChangeListener {
            // TODO 滑动过程中一直回调
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                // TODO 当前fragment
                val currentFragment = listFragment[position]
                currentFragment.list.forEach { view ->
                    val tag = view.getTag(view.id)

                    (tag as? C3Bean)?.let {
                        // 入场
                        view.translationX = -it.parallaxTransformInX * positionOffsetPixels
                        view.translationY = -it.parallaxTransformInY * positionOffsetPixels
                        view.rotation = -it.parallaxRotate * 360 * positionOffset


                        view.scaleX =
                            1 + it.parallaxZoom - (it.parallaxZoom * positionOffset)
                        view.scaleY =
                            1 + it.parallaxZoom - (it.parallaxZoom * positionOffset)

                    }
                }

                // TODO 下一个fragment
                // 防止下标越界
                if (position + 1 < listFragment.size) {
                    val nextFragment = listFragment[position + 1]
                    nextFragment.list.forEach { view ->
                        val tag = view.getTag(view.id)

                        (tag as? C3Bean)?.also {
                            view.translationX =
                                it.parallaxTransformInX * (width - positionOffsetPixels)
                            view.translationY =
                                it.parallaxTransformInY * (height - positionOffsetPixels)

                            view.rotation = it.parallaxRotate * 360 * positionOffset

                            view.scaleX = (1 + it.parallaxZoom * positionOffset)
                            view.scaleY = (1 + it.parallaxZoom * positionOffset)
                        }
                    }
                }
            }

            //TODO 当页面切换完成时候调用 返回当前页面位置
            override fun onPageSelected(position: Int) {
                block(listFragment.size, position, listFragment[position])
                Log.e("szjParallaxViewPager", "onPageSelected:$position")
            }

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    SCROLL_STATE_IDLE -> {
                        Log.e("szjParallaxViewPager", "onPageScrollStateChanged:页面空闲中..")
                    }
                    SCROLL_STATE_DRAGGING -> {
                        Log.e("szjParallaxViewPager", "onPageScrollStateChanged:拖动中..")
                    }
                    SCROLL_STATE_SETTLING -> {
                        Log.e("szjParallaxViewPager", "onPageScrollStateChanged:拖动停止了..")
                    }
                }
            }
        })
    }

    private inner class ParallaxBlockAdapter(
        private val list: List<Fragment>,
        fm: FragmentManager
    ) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int = list.size
        override fun getItem(position: Int) = list[position]
    }
}