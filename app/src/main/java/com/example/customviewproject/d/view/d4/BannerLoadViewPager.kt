package com.example.customviewproject.d.view.d4

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 *
 * @ClassName: BannerLoadViewPager
 * @Author: 史大拿
 * @CreateDate: 10/27/22$ 11:14 AM$
 * TODO
 */
open class BannerLoadViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    private var slide = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (slide) true else super.onTouchEvent(ev)
    }

    open fun setSlide(slide: Boolean) {
        this.slide = slide
    }
}