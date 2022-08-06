package com.example.customviewproject

import android.os.Bundle
import com.example.customviewproject.a.activity.A1Activity
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.ActivityMainBinding
import com.example.customviewproject.ext.szj.szj

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.title = "自定义View系列"

        binding.bt1.szj.click {
            szj.jumpActivity(it, A1Activity::class.java)
        }
    }

}