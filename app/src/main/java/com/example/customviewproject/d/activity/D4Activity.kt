package com.example.customviewproject.d.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.customviewproject.R
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.d.view.d4.BannerLoadAdapter
import com.example.customviewproject.d.view.d4.BaseBannerLoadAdapter
import com.example.customviewproject.databinding.D4ActivityBinding
import com.example.customviewproject.ext.toast

/**
 *
 * @ClassName: D4Activity
 * @Author: 史大拿
 * @CreateDate: 10/27/22$ 11:09 AM$
 * TODO viewPager + 加载更多
 */
class D4Activity : BaseActivity<D4ActivityBinding>() {
    @SuppressLint("InflateParams")
    override fun initView(savedInstanceState: Bundle?) {

        val list = listOf(
            R.layout.c3_1_item,
            R.layout.c3_2_item,
            R.layout.c3_3_item,
            R.layout.c3_4_item
        )

        val adapter = BannerLoadAdapter(
            list,
            R.layout.load_view
        )

        adapter.click = object : BaseBannerLoadAdapter.Click {
            override fun loadView(view: View) {
                view.findViewById<TextView>(R.id.load_view).text = "加载更多!"
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun loadMore() {
                "加载更多" toast applicationContext
            }
        }

        binding.bannerView.setAdapter(adapter)

    }
}