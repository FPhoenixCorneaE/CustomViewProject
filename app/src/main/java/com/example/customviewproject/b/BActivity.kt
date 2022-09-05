package com.example.customviewproject.b

import android.os.Bundle
import com.example.customviewproject.R
import com.example.customviewproject.b.activity.B1Activity
import com.example.customviewproject.b.activity.B2Activity
import com.example.customviewproject.b.activity.B3Activity
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.BActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity
import com.example.customviewproject.ext.jumpLayoutActivity

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

        binding.bt2.click {
            jumpActivity(it, B2Activity::class.java)
        }

        binding.bt3.click {
            jumpActivity(it, B3Activity::class.java)
//            jumpLayoutActivity(it, R.layout.b3_activity)
        }

        binding.bt4.click {
            jumpLayoutActivity(it, R.layout.b4_activity)
        }
    }
}