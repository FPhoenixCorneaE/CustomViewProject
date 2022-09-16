package com.example.customviewproject.b.view.b7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customviewproject.R
import com.example.customviewproject.adapter.QQDragAdapter
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.toast

/**
 *
 * @ClassName: DropDownAdapter
 * @Author: 史大拿
 * @CreateDate: 9/15/22$ 3:17 PM$
 * TODO
 */
class DropDownAdapter(private val context: Context) : BaseDropDownAdapter() {

    val list = listOf("区域", "租金", "户型", "筛选")

    override fun count(): Int = list.size

    override fun tabView(position: Int, parent: ViewGroup) = let {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.b7_tab_item_layout, parent, false) as TextView
        view.text = list[position]
        view
    }

    override fun contentView(position: Int, parent: ViewGroup): View = let {
        // 测试数据
        if (position == 1) {
            val recyclerView = LayoutInflater.from(context)
                .inflate(R.layout.item_recycler_view, parent, false) as RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = QQDragAdapter()
            recyclerView
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.b7_menu_item_layout, parent, false)
            view.findViewById<TextView>(R.id.bt).also {
                it.text = list[position]
                it.click { _ ->
                    "${it.text}" toast context
                }
            }
            view
        }
    }
}