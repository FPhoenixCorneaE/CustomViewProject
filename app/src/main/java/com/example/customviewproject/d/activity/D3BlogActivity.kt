package com.example.customviewproject.d.activity

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.d.view.d3.UnLockAdapter
import com.example.customviewproject.databinding.D3BlogActivityBinding

/**
 *
 * @ClassName: D3BlogActivity
 * @Author: 史大拿
 * @CreateDate: 9/14/22$ 10:40 AM$
 * TODO
 */
class D3BlogActivity : BaseActivity<D3BlogActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.unlockView.password = arrayListOf(1, 2, 3, 4)

        binding.unlockView.adapter = UnLockAdapter()
    }
}