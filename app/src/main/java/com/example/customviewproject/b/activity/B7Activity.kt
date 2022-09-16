package com.example.customviewproject.b.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.customviewproject.b.view.b7.BaseDropDownAdapter
import com.example.customviewproject.b.view.b7.DropDownAdapter
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.B7ActivityBinding

/**
 *
 * @ClassName: B7Activity
 * @Author: 史大拿
 * @CreateDate: 9/15/22$ 2:35 PM$
 * TODO
 */
class B7Activity : BaseActivity<B7ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        val dropDownAdapter = DropDownAdapter(this)
        binding.dropDownSelectView.adapter = dropDownAdapter
        dropDownAdapter.blockClick = object : BaseDropDownAdapter.Click {
            override fun click(view: View, oldView: View?, position: Int, oldPosition: Int) {
                Log.e("szjActivity", "position:$position\toldPosition:$oldPosition")
                view.setBackgroundColor(Color.RED)
//                "点击了$position" toast applicationContext
                oldView?.also {
                    it.setBackgroundColor(Color.WHITE)
                }
            }


            override fun openView(view: View, position: Int) {
                Log.e("szjActivity", "openView:$position")
                view.also {
                    it.setBackgroundColor(Color.RED)
                }
            }

            override fun closeView(view: View, position: Int) {
                Log.e("szjActivity", "closeView:$position")
                view.also {
                    it.setBackgroundColor(Color.WHITE)
                }
            }
        }
    }
}