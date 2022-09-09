package com.example.customviewproject.d.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.c.view.c2.C2Adapter
import com.example.customviewproject.databinding.D2ActivityBinding
import com.example.customviewproject.ext.toast
import com.google.android.material.chip.Chip

/**
 *
 * @ClassName: D2Activity
 * @Author: 史大拿
 * @CreateDate: 9/9/22$ 1:48 PM$
 * TODO ViewDragHelper 实现下滑效果
 */
class D2Activity : BaseActivity<D2ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        /**
         *
         * 下滑可以看到效果!!!!!!!!!!!!!!!
         * 下滑可以看到效果!!!!!!!!!!!!!!!
         * 下滑可以看到效果!!!!!!!!!!!!!!!
         * 下滑可以看到效果!!!!!!!!!!!!!!!
         * 下滑可以看到效果!!!!!!!!!!!!!!!
         * 下滑可以看到效果!!!!!!!!!!!!!!!
         * 下滑可以看到效果!!!!!!!!!!!!!!!
         *
         */
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = C2Adapter()
        }

        binding.chipGroup.setOnCheckedChangeListener { group, _ ->
            group.checkedChipIds.map {
                findViewById<Chip>(it).text.toString()
            } toast applicationContext
        }
    }
}