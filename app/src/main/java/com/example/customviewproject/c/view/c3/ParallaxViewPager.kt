package com.example.customviewproject.c.view.c3

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.customviewproject.c.fragment.c3.C3Fragment
import java.util.ArrayList

/**
 *
 * @ClassName: ParallaxViewPager
 * @Author: 史大拿
 * @CreateDate: 8/25/22$ 4:31 PM$
 * TODO 视差动画
 */
class ParallaxViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private val listFragment = arrayListOf<C3Fragment>()

    /*
     * 作者:史大拿
     * 创建时间: 8/29/22 9:47 AM
     * TODO
     * @param2 list: 所有layout布局
     * @param3 block参数: @param1 :Fragment总长度
     *                   @param2 :当前切换下标
     *                   @param3 :当前切换到的fragment
     */
    fun setLayout(
        fm: FragmentManager,
        @LayoutRes list: ArrayList<Int>,
        block: (Int, Int, Fragment) -> Unit,
    ) {
        listFragment.clear()
        // 加载fragment
        list.map {
            C3Fragment.instance(it)
        }.forEach {
            listFragment.add(it)
        }

        adapter = ParallaxAdapter(listFragment, fm)


        // 监听变化
        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                Log.e("szjParallaxViewPager",
                    "onPageScrolled:position$position\tpositionOffset:${positionOffset}\tpositionOffsetPixels:${positionOffsetPixels}")
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
//                        Log.i("szjNextFragment", "${view::class.java.simpleName}\t$tag")

                        (tag as? C3Bean)?.let {
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
                Log.e("szjParallaxViewPager", "onPageSelected:$position")
                block(listFragment.size, position, listFragment[position])
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

    inner class ParallaxAdapter(private val list: List<Fragment>, fm: FragmentManager) :
        FragmentPagerAdapter(fm) {
        override fun getCount(): Int = list.size
        override fun getItem(position: Int) = list[position]
    }
}