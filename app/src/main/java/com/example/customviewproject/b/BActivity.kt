package com.example.customviewproject.b

import android.os.Bundle
import com.example.customviewproject.b.activity.B1Activity
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.BActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity

/**
 *
 * @ClassName: BActivity
 * @Author: 史大拿
 * @CreateDate: 8/11/22$ 2:37 PM$
 * TODO
 */
class BActivity : BaseActivity<BActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.bt1.click {
            jumpActivity(it, B1Activity::class.java)
        }
    }
}