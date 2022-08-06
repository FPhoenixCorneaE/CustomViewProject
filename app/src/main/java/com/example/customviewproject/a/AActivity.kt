package com.example.customviewproject.a

import android.os.Bundle
import com.example.customviewproject.a.activity.A0Activity
import com.example.customviewproject.a.activity.A1Activity
import com.example.customviewproject.a.activity.A2Activity
import com.example.customviewproject.a.activity.A3Activity
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.AActivityBinding
import com.example.customviewproject.ext.jumpActivity

/**
 *
 * @ClassName: AActivity
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 5:29 PM$
 * TODO 入门系列
 */
class AActivity : BaseActivity<AActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        binding.bt0.setOnClickListener {
            jumpActivity(it, A0Activity::class.java)
        }

        binding.bt1.setOnClickListener {
            jumpActivity(it, A1Activity::class.java)
        }

        binding.bt2.setOnClickListener {
            jumpActivity(it, A2Activity::class.java)
        }

        binding.bt3.setOnClickListener {
            jumpActivity(it, A3Activity::class.java)
        }


    }
}