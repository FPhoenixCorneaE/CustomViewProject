package com.example.customviewproject.d.view.d4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @ClassName: ViewPagerAdapter
 * @Author: 史大拿
 * @CreateDate: 10/31/22$ 9:49 AM$
 * TODO
 */
class ViewPagerAdapter(private val listId: List<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {


    // 将type替换成position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(listId[position], parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = listId.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}