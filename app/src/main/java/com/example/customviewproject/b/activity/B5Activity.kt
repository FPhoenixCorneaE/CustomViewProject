package com.example.customviewproject.b.activity

import android.os.Bundle
import android.view.View
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.B5ActivityBinding

/**
 *
 * @ClassName: B5Activity
 * @Author: 史大拿
 * @CreateDate: 9/5/22$ 4:04 PM$
 * TODO 字母导航栏
 */
class B5Activity : BaseActivity<B5ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.letterView.setCallMoveBlock { position, value ->
            binding.tv.visibility = if (position == -1) {
                binding.moveTv.visibility = View.VISIBLE
                View.GONE
            } else {
                binding.moveTv.visibility = View.GONE
                View.VISIBLE
            }
            binding.tv.text = value
        }
    }
}