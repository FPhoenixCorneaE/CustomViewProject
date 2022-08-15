package com.example.customviewproject.b.activity

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.B2ActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity

/**
 *
 * @ClassName: B2Activity
 * @Author: 史大拿
 * @CreateDate: 8/12/22$ 7:48 PM$
 * TODO 多点触控
 */
class B2Activity : BaseActivity<B2ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.bt0.click {
            jumpActivity(it, B20Activity::class.java)
        }
        binding.bt1.click {
            jumpActivity(it, B21Activity::class.java)
        }
        binding.bt2.click {
            jumpActivity(it, B22Activity::class.java)
        }
        binding.bt3.click {
            jumpActivity(it, B23Activity::class.java)
        }
        binding.bt4.click {
            jumpActivity(it, B24Activity::class.java)
        }
    }
}