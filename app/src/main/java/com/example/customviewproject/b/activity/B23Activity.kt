package com.example.customviewproject.b.activity

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.B23ActivityBinding
import com.example.customviewproject.ext.click

/**
 *
 * @ClassName: B23Activity
 * @Author: 史大拿
 * @CreateDate: 8/15/22$ 10:11 AM$
 * TODO 多点触控 各自为战型
 */
class B23Activity : BaseActivity<B23ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.clearBt.click {
            binding.touchView.clear()
        }

    }
}