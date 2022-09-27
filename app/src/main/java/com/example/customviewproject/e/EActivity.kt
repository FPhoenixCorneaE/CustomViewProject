package com.example.customviewproject.e

import android.os.Bundle
import com.example.customviewproject.R
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.EActivityBinding
import com.example.customviewproject.e.e1.E1Activity
import com.example.customviewproject.e.e1_blog.E1BlogActivity
import com.example.customviewproject.e.e2.E2Activity
import com.example.customviewproject.e.e3.E3Activity
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity
import com.example.customviewproject.ext.jumpTabActivity

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

        binding.bt2Blog.click {
            jumpTabActivity(
                it,
                listOf("矩形图表绘制" to R.layout.e2_blog_item),
            )
        }

        binding.bt3.click {
            jumpActivity(it, E3Activity::class.java)
        }

        binding.bt4.click {
            jumpTabActivity(
                it,
                listOf("饼状图" to R.layout.e4_item),
            )
        }
    }
}