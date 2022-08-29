package com.example.customviewproject.c.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.customviewproject.R
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.C3ActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.toast

/**
 *
 * @ClassName: C3Activity
 * @Author: 史大拿
 * @CreateDate: 8/25/22$ 4:29 PM$
 * TODO 视差动画
 */
class C3Activity : BaseActivity<C3ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        binding.viewpager.setLayout(
            supportFragmentManager,
            arrayListOf(R.layout.c3_1_item,
                R.layout.c3_2_item,
                R.layout.c3_3_item,
                R.layout.c3_4_item)) { size, position, fragment ->
            Log.e("szjFragment执行了", "size:$size\tposition:${position}\t:fragment:${fragment}")

            if (position == size - 1) {
                fragment.view?.also { view ->
                    view.findViewById<Button>(R.id.jumpBtn).click {
                        "点击了" toast this@C3Activity
                    }
                }
            }

        }
    }
}