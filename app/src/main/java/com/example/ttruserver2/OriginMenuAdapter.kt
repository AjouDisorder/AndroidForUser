package com.example.ttruserver2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ttruserver2.models.OriginMenuModel

class OriginMenuAdapter(val originMenuList: ArrayList<OriginMenuModel>) : RecyclerView.Adapter<OriginMenuAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OriginMenuAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.originmenu_list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return originMenuList.size
    }

    override fun onBindViewHolder(holder: OriginMenuAdapter.CustomViewHolder, position: Int) {
        holder.originMenuTitle.text = originMenuList.get(position).originMenuTitle
        holder.originMenuPrice.text = originMenuList.get(position).originMenuPrice.toString()
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val originMenuTitle = itemView.findViewById<TextView>(R.id.tv_originMenuTitle)
        val originMenuPrice = itemView.findViewById<TextView>(R.id.tv_originMenuPrice)
    }
}