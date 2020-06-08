package com.example.ttruserver2.BottomTab.Coupon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ttruserver2.R
import com.example.ttruserver2.models.BottomCouponContentsListModel

class BottomCouponListViewAdapter (val context: Context, val list: ArrayList<BottomCouponContentsListModel>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder : ViewHolder

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.coupon_food_info_listviev, null)
            holder = ViewHolder()
            holder.coupon_food_title = view.findViewById(R.id.bottom_coupon_food_name)
            holder.coupon_food_restaurant_name = view.findViewById(R.id.bottom_coupon_restaurant_name)
            holder.coupon_food_restaurant_address = view.findViewById(R.id.bottom_coupon_restaurant_address)
            holder.coupon_food_totalprice = view.findViewById(R.id.bottom_coupon_total_price)
            holder.coupon_food_pay_num = view.findViewById(R.id.bottom_coupon_pay_num)
            holder.coupon_food_method = view.findViewById(R.id.bottom_coupon_method)

            view.tag = holder
        }else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val item = list[position]
//        holder.coupon_food_img?.setImageResource(item.image)
        var method_ : String? = null
        if(item.method == "forhere") {
            method_ = "매장 식사"
        }else if(item.method == "takeout"){
            method_ = "포장"
        }

        holder.coupon_food_title?.text = "음식명: "+item.menuName
        holder.coupon_food_restaurant_name?.text = "가게이름: "+item.restaurantTitle
        holder.coupon_food_restaurant_address?.text = "주소: "+item.address
        holder.coupon_food_totalprice?.text = "총액: " + item.totalPrice.toString()
        holder.coupon_food_pay_num?.text = "구매 수량: "+item.quantity.toString()
        holder.coupon_food_method?.text = "식사 방식: "+method_

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
//        var coupon_food_img : ImageView? = null
        var coupon_food_title : TextView? = null
        var coupon_food_restaurant_name : TextView? = null
        var coupon_food_restaurant_address : TextView? = null
        var coupon_food_totalprice : TextView? = null
        var coupon_food_pay_num : TextView? = null
        var coupon_food_method : TextView? = null
    }

}