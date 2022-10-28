package com.example.customviewproject.d.view.d4

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BannerLoadViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) :
         FragmentPagerAdapter(fm) {
        override fun getCount() = fragments.size

        override fun getItem(position: Int) = fragments[position]
    }