package com.example.customviewproject.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.customviewproject.R
import com.example.customviewproject.databinding.TabActivityBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 *
 * @ClassName: BaseTabActivity
 * @Author: 史大拿
 * @CreateDate: 8/29/22$ 3:17 PM$
 * TODO
 */
abstract class BaseTabActivity : BaseActivity<TabActivityBinding>() {

    @SuppressLint("InflateParams")
    override fun customViewBinDing(): TabActivityBinding {
        val view = LayoutInflater.from(this).inflate(R.layout.tab_activity, null, false)
        return TabActivityBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initHeadView(binding.headFrameLayout)

        val data = initData()
        val list = data.map { BaseTabFragment.instance(it.second) }.toList()

        binding.viewpager2.adapter = TabAdapter(supportFragmentManager, list)

        //绑定tabLayout和viewPager
        TabLayoutMediator(
            binding.tableLayout, binding.viewpager2, true, true
        ) { tab, position ->
            tab.text = data[position].first
        }.attach()
    }


    inner class TabAdapter(fragmentManager: FragmentManager, val list: List<Fragment>) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int = list.size
        override fun createFragment(position: Int) = list[position]
    }

    abstract fun initHeadView(vp: ViewGroup)

    /*
     * 作者:史大拿
     * 创建时间: 8/29/22 3:59 PM
     * TODO
     * 自定义数据
     */
    abstract fun initData(): List<Pair<String, Int>>
}