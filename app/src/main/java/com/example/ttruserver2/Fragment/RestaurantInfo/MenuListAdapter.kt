package com.example.ttruserver2.Fragment.RestaurantInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ttruserver2.Fragment.ContentsListModel
import com.example.ttruserver2.R

class MenuListAdapter (val context: Context, val list: ArrayList<ContentsListModel>) : BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view : View = LayoutInflater.from(context).inflate(R.layout.food_info_listviev, null)

        val view : View
        val holder : ViewHolder
        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.food_info_listviev, null)
            holder = ViewHolder()
            holder.view_image1 = view.findViewById(R.id.lv_img_area)
            holder.view_text1 = view.findViewById(R.id.lv_textview_1)
            holder.view_text2 = view.findViewById(R.id.lv_textview_2)
            holder.view_text3 = view.findViewById(R.id.lv_textview_3)

            view.tag = holder

        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }
        val item = list[position]

        holder.view_text1?.text = item.title//음식 리스트 내용 받아오기
        holder.view_text2?.text = item.price.toString()//음식 리스트 내용 받아오기
        holder.view_text3?.text = item.category//음식 리스트 내용 받아오기



        return view
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    private class ViewHolder{
        var view_image1 : ImageView? = null
        var view_text1 : TextView? = null
        var view_text2 : TextView? = null
        var view_text3 : TextView? = null
    }

}