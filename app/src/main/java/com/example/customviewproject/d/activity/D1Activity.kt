package com.example.customviewproject.d.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customviewproject.R
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.adapter.QQDragAdapter
import com.example.customviewproject.databinding.D1ActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.toast

/**
 *
 * @ClassName: D1Activity
 * @Author: 史大拿
 * @CreateDate: 9/7/22$ 7:38 PM$
 * TODO 酷狗侧滑
 */
class D1Activity : BaseActivity<D1ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        draw()

        content()
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/8/22 10:53 AM
     * TODO 侧滑内容
     */
    private fun draw() {
        findViewById<View>(R.id.close_tv).click {
            "退出" toast this
            binding.drawerView.closeDrawer()
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 9/8/22 10:53 AM
     * TODO 主页内容
     */
    private fun content() {

        findViewById<View>(R.id.banner_tv).click {
            "banner" toast this
            binding.drawerView.openDrawer()
        }
//        binding.drawerView.styleMode = 2

        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(this@D1Activity)
            adapter = QQDragAdapter()
        }

    }
}