package com.example.customviewproject.c

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.c.activity.*
import com.example.customviewproject.c.view.c5.C5Activity
import com.example.customviewproject.databinding.CActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity

/**
 *
 * @ClassName: CActivity
 * @Author: 史大拿
 * @CreateDate: 8/17/22$ 10:31 AM$
 * TODO 小进阶
 */
class CActivity : BaseActivity<CActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        binding.bt1.click {
            jumpActivity(it, C1Activity::class.java)
        }

        binding.bt2.click {
            jumpActivity(it, C2Activity::class.java)
        }

        binding.bt3.click {
            jumpActivity(it, C3Activity::class.java)
        }

        binding.bt3Blog.click {
            jumpActivity(it, C3BlogActivity::class.java)
        }

        binding.bt4.click {
            jumpActivity(it, C4Activity::class.java)
        }

        binding.bt5.click {
            jumpActivity(it, C5Activity::class.java)
        }
    }
}