package com.example.customviewproject.c.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.adapter.QQDragAdapter
import com.example.customviewproject.c.view.c2.DragBubbleUtil
import com.example.customviewproject.databinding.C2ActivityBinding

/**
 *
 * @ClassName: C2Activity
 * @Author: 史大拿
 * @CreateDate: 8/18/22$ 4:07 PM$
 * TODO 任何控件都能拖拽
 */
class C2Activity : BaseActivity<C2ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {


        DragBubbleUtil(binding.bt1).bind()
        DragBubbleUtil(binding.view).bind()
        DragBubbleUtil(binding.tv).bind()


        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = QQDragAdapter()
    }
}