package com.example.ttruserver2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.gridview_item.view.*

class GridViewAdapter(val context: Context, val girid_img_list:Array<Int>, val text_list:Array<String>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.gridview_item, null)

        view.gridview_text.text = text_list[position]
        view.gridview_img.setImageResource(girid_img_list[position])

        return view
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return girid_img_list.size
    }

}