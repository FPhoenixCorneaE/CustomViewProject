package com.example.customviewproject.c.activity

import android.os.Bundle
import android.util.Log
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.C4ActivityBinding

/**
 *
 * @ClassName: C4Activity
 * @Author: 史大拿
 * @CreateDate: 8/31/22$ 3:19 PM$
 * TODO LoadingView
 */
class C4Activity : BaseActivity<C4ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        val a = 11.052f
        val b = 15.213f
        Log.e("szjFloat", "${a / b}")
    }
}