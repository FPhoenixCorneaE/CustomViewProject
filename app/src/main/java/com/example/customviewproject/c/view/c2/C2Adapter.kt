package com.example.customviewproject.c.view.c2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customviewproject.R

/**
 *
 * @ClassName: C2Adapter
 * @Author: 史大拿
 * @CreateDate: 8/19/22$ 10:41 AM$
 * TODO
 */
class C2Adapter : RecyclerView.Adapter<C2Adapter.ViewHolder>() {

    private val list = arrayListOf<String>()

    init {
        list.addAll((0..100).map { "$it" }.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.c2_item, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = list[position]
        DragBubbleUtil(holder.tv).bind()
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView = itemView.findViewById(R.id.tv)

    }


}