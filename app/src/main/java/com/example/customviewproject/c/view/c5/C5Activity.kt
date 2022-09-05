package com.example.customviewproject.c.view.c5

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.C5LayoutBinding
import com.example.customviewproject.ext.click

/**
 *
 * @ClassName: C5Activity
 * @Author: 史大拿
 * @CreateDate: 9/5/22$ 10:00 AM$
 * TODO 抢红包效果
 */
class C5Activity : BaseActivity<C5LayoutBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.bt.click {
            binding.redView.reset()
            binding.redView.processBarAnimator.start()
        }
    }
}