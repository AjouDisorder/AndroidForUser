package com.example.ttruserver2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search_log_item.view.*

class SearchBarAdapter (val searchLogList: ArrayList<String>, val selectedIconType: Int) : RecyclerView.Adapter<SearchBarAdapter.CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBarAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_log_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.btn_xShape.setOnClickListener {
                val curPos: Int = adapterPosition
                if (selectedIconType == 0){
                    UserData.deleteSearchedMenuLog(curPos)
                }else{
                    UserData.deleteSearchedRestaurantLog(curPos)
                }
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition, searchLogList.size)
            }
        }
    }

    override fun getItemCount(): Int {
        return searchLogList.size
    }

    override fun onBindViewHolder(holder: SearchBarAdapter.CustomViewHolder, position: Int) {
        holder.title.text = searchLogList.get(position)
    }
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.tv_searchLog)
    }
}