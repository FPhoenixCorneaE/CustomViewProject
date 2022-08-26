package com.example.customviewproject.c.activity

import android.os.Bundle
import android.util.Log
import com.example.customviewproject.R
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.C3ActivityBinding

/**
 *
 * @ClassName: C3Activity
 * @Author: 史大拿
 * @CreateDate: 8/25/22$ 4:29 PM$
 * TODO 视差动画
 */
class C3Activity : BaseActivity<C3ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        binding.viewpager.setLayout(
            supportFragmentManager,
            arrayListOf(R.layout.c3_1_item,
                R.layout.c3_2_item,
                R.layout.c3_3_item,
                R.layout.c3_4_item))
    }
}