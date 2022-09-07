package com.example.customviewproject.d.activity

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.D1ActivityBinding

/**
 *
 * @ClassName: D1Activity
 * @Author: 史大拿
 * @CreateDate: 9/7/22$ 7:38 PM$
 * TODO 酷狗侧滑
 */
class D1Activity : BaseActivity<D1ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        actionBar?.hide()
        supportActionBar?.hide()
    }
}