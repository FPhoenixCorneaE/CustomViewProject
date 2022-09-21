package com.example.customviewproject.e.e1

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.E1ActivityBinding
import com.example.customviewproject.e.BaseChatAdapter
import com.example.customviewproject.e.e1.view.E1ChartView

/**
 *
 * @ClassName: E1Activity
 * @Author: 史大拿
 * @CreateDate: 9/16/22$ 2:26 PM$
 * TODO
 */
class E1Activity : BaseActivity<E1ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        binding.e1ChartView.adapter = object : BaseChatAdapter() {

            // 实心
            override fun fillStyle(): E1ChartView.FillStyle = E1ChartView.FillStyle.STROKE

            // 绘制虚线
            override fun lineStyle(): E1ChartView.LineStyle = E1ChartView.LineStyle.DOTTED_LINE

            // 点的样式
            override fun pointStyle(): E1ChartView.PointStyle = E1ChartView.PointStyle.ROUND

            // 是否绘制网格
            override fun isDrawGrid(): Boolean = true

            override fun horizontalCount(): Int = 20

            override fun verticalCount(): Int = 10
        }


        binding.e1ChartView.originList = arrayListOf(
            70, 80, 100, 222, 60,
            70, 80, 100, 222, 60,
            777, 210, 100, 2222, 80,
            70, 880, 100, 222, 700
        )
    }
}