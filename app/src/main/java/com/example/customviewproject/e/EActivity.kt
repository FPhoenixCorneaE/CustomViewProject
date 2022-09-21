package com.example.customviewproject.e

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.EActivityBinding
import com.example.customviewproject.e.e1.E1Activity
import com.example.customviewproject.e.e1_blog.E1BlogActivity
import com.example.customviewproject.e.e2.E2Activity
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity

/**
 *
 * @ClassName: EActivity
 * @Author: 史大拿
 * @CreateDate: 9/16/22$ 2:24 PM$
 * TODO 图表activity
 */
class EActivity : BaseActivity<EActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.bt1.click {
            jumpActivity(it, E1Activity::class.java)
        }

        binding.bt1Blog.click {
            jumpActivity(it, E1BlogActivity::class.java)
        }

        binding.bt2.click {
            jumpActivity(it, E2Activity::class.java)
        }
    }
}