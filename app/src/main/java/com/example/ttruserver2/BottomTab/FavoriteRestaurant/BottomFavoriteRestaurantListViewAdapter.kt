package com.example.ttruserver2.BottomTab.FavoriteRestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ttruserver2.R

class BottomFavoriteRestaurantListViewAdapter (val context: Context, val list: ArrayList<BottomFavoriteRestaurantContentListModel>):BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder : ViewHolder

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.favorit_restaurant_info_listview, null)
            holder = ViewHolder()
            holder.favorite_restaurant_food_img = view.findViewById(R.id.bottom_favo_restau_food_img)
            holder.favorite_restaurant_food_title = view.findViewById(R.id.bottom_favo_restau_restaurant_name)
            holder.favorite_restaurant_food_price = view.findViewById(R.id.bottom_favo_restau_food_price)
            holder.favorite_restaurant_food_category = view.findViewById(R.id.bottom_favo_restau_food_name)
            holder.favorite_restaurant_aveGrade = view.findViewById(R.id.gagqgr)
            holder.favorite_restaurant_saleOn = view.findViewById(R.id.qweqergnfawf)

            view.tag = holder
        }else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }
        val item = list[position]
        holder.favorite_restaurant_food_img?.setImageResource(item.image)
        holder.favorite_restaurant_food_title?.text = item.title
        holder.favorite_restaurant_food_price?.text = item.restaurnat_address
        holder.favorite_restaurant_food_category?.text = item.category
        holder.favorite_restaurant_aveGrade?.text = item.avrGrade
        holder.favorite_restaurant_saleOn?.text = item.saleOn

        return view
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    private class ViewHolder{
        var favorite_restaurant_food_img : ImageView? = null
        var favorite_restaurant_food_title : TextView? = null
        var favorite_restaurant_food_price : TextView? = null
        var favorite_restaurant_food_category : TextView? = null
        var favorite_restaurant_aveGrade : TextView? = null
        var favorite_restaurant_saleOn : TextView? = null

    }

}