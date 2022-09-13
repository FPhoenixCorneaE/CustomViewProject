package com.example.customviewproject.d.activity

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.d.view.d3.JiuGonGeUnLockView
import com.example.customviewproject.d.view.d3.UnLockAdapter
import com.example.customviewproject.databinding.D3ActivityBinding
import com.example.customviewproject.ext.toast

/**
 *
 * @ClassName: D3Activity
 * @Author: 史大拿
 * @CreateDate: 9/13/22$ 9:30 AM$
 * TODO
 */
class D3Activity : BaseActivity<D3ActivityBinding>() {


    override fun initView(savedInstanceState: Bundle?) {

        val unLockAdapter = UnLockAdapter()
        binding.unlockView.adapter = unLockAdapter
        setData(unLockAdapter.getNumber())


        binding.btn.setOnClickListener {
            setData(unLockAdapter.getNumber())
            binding.unlockView.clear()
        }

        binding.unlockView.resultClick =
            JiuGonGeUnLockView.Click { pwd, inputPwd, isSuccess ->
                if (isSuccess) {
                    "输入成功:${pwd}" toast applicationContext
                } else {
                    "输入失败\n输入密码为:${inputPwd}" toast applicationContext
                }
            }
    }

    // 设置密码
    private fun setData(number: Int) {
        val r = getRandom(number)
        binding.tv.text = r.toString()
        binding.unlockView.password = r
    }

    private fun getRandom(number: Int) = let {
        val list = arrayListOf<Int>()
        // 随机4位数

        while (list.size != number * number / 3) {
            val random = (1 until number * number).random()
            if (!list.contains(random)) {
                list.add(random)
            }
        }
        list
    }
}