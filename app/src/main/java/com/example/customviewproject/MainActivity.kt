package com.example.customviewproject

import android.os.Bundle
import com.example.customviewproject.a.AActivity
import com.example.customviewproject.b.BActivity
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.c.CActivity
import com.example.customviewproject.d.DActivity
import com.example.customviewproject.databinding.ActivityMainBinding
import com.example.customviewproject.ext.*

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.title = "自定义View系列"

        binding.bt1.click {
            jumpActivity(it, AActivity::class.java)
        }

        binding.bt2.click {
            jumpActivity(it, BActivity::class.java)
        }

        binding.bt3.click {
            jumpActivity(it, CActivity::class.java)
        }

        binding.bt4.click {
            jumpActivity(it, DActivity::class.java)
        }
    }
}