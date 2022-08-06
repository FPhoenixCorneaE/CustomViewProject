package com.example.customviewproject

import android.os.Bundle
import com.example.customviewproject.a.AActivity
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.ActivityMainBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.title = "自定义View系列"

        binding.bt1.click {
            jumpActivity(it, AActivity::class.java)
        }

    }
}