package com.example.customviewproject.base

import android.os.Bundle
import android.view.ViewGroup
import com.example.customviewproject.databinding.BaseLayoutActivityBinding

/**
 *
 * @ClassName: BaseLayoutActivity
 * @Author: 史大拿
 * @CreateDate: 9/3/22$ 11:24 AM$
 * TODO zhi zh
 */
class BaseLayoutActivity : BaseActivity<BaseLayoutActivityBinding>() {
    companion object {
        const val LAYOUT_ID = "layout_id"
    }

    override fun initView(savedInstanceState: Bundle?) {
        val vg = findViewById<ViewGroup>(android.R.id.content)
        intent.getIntExtra(LAYOUT_ID, -1).apply {
            layoutInflater.inflate(this, vg)
        }
    }
}