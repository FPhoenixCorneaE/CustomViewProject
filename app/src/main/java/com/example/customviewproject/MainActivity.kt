package com.example.customviewproject

import android.os.Bundle
import com.example.customviewproject.a.AActivity
import com.example.customviewproject.b.BActivity
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.c.CActivity
import com.example.customviewproject.d.DActivity
import com.example.customviewproject.databinding.ActivityMainBinding
import com.example.customviewproject.e.EActivity
import com.example.customviewproject.ext.*
import com.example.customviewproject.f.FActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.title = "自定义View系列"

        // 基础
        binding.bt1.click {
            jumpActivity(it, AActivity::class.java)
        }

        // 入门
        binding.bt2.click {
            jumpActivity(it, BActivity::class.java)
        }

        // 小进阶
        binding.bt3.click {
            jumpActivity(it, CActivity::class.java)
        }

        // 侧滑
        binding.bt4.click {
            jumpActivity(it, DActivity::class.java)
        }

        // 图表
        binding.bt5.click {
            jumpActivity(it, EActivity::class.java)
        }

        // 运动
        binding.bt6.click {
            jumpActivity(it, FActivity::class.java)
        }
    }
}