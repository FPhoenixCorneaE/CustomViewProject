package com.example.customviewproject.f

import android.os.Bundle
import com.example.customviewproject.R
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.FActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpTabActivity

/**
 *
 * @ClassName: FActivity
 * @Author: 史大拿
 * @CreateDate: 9/23/22$ 7:55 PM$
 * TODO 运动
 */
class FActivity : BaseActivity<FActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.bt1.click {
            jumpTabActivity(
                it, listOf(
                    "左右滚动" to R.layout.f11_item,
                    "范围滚动" to R.layout.f12_item,
                    "随机滚动" to R.layout.f13_item,
                )
            )
        }
    }
}