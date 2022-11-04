package com.example.customviewproject.d

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.d.activity.*
import com.example.customviewproject.databinding.DActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity

/**
 *
 * @ClassName: DActivity
 * @Author: 史大拿
 * @CreateDate: 9/7/22$ 7:39 PM$
 * TODO
 */
class DActivity : BaseActivity<DActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.bt1.click {
            jumpActivity(it, D1Activity::class.java)
        }

        binding.bt2.click {
            jumpActivity(it, D2Activity::class.java)
        }

        binding.bt3.click {
            jumpActivity(it, D3Activity::class.java)
        }

        binding.bt4.click {
            jumpActivity(it, D3BlogActivity::class.java)
        }

        /*
         * 作者:史大拿
         * 创建时间: 11/1/22 3:10 PM
         * TODO viewPager加载更多
         */
        binding.bt5.click {
            jumpActivity(it, D4Activity::class.java)
        }
    }
}