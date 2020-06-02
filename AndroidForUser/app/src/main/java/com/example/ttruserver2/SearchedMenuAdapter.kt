package com.example.ttruserver2

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ttruserver2.models.SearchedMenuModel
import com.example.ttruserver2.models.SearchedRestaurantModel

class SearchedMenuAdapter(private val menuList: ArrayList<SearchedMenuModel>) : RecyclerView.Adapter<SearchedMenuAdapter.CustomViewHolder>(){

    val menuTypeToIcons = hashMapOf( "치킨&피자" to R.drawable.menu_chickenpizza, "족발&보쌈" to R.drawable.menu_jokbal,
        "돈까스&일식" to R.drawable.menu_japan, "세계음식" to R.drawable.menu_nation, "햄버거" to R.drawable.menu_hambur,
        "밥류" to R.drawable.menu_rice, "카페&빵&디저트" to R.drawable.menu_cafe, "육고기" to R.drawable.menu_meat,
        "면" to R.drawable.menu_noodle, "분식&야식" to R.drawable.menu_snack, "찜&탕&찌개" to R.drawable.menu_soup,
        "반찬&과일" to R.drawable.menu_fruit, "떡&기타" to R.drawable.menu_ricecake,
        "샐러드&다이어트" to R.drawable.menu_salad, "편의점" to R.drawable.menu_convstore)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedMenuAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_list_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos: Int = adapterPosition
                val selectedMenu: SearchedMenuModel = menuList[curPos]
                val intent = Intent(parent.context, SearchedMenuDetailActivity::class.java)
                intent.putExtra("selectedMenu", selectedMenu)
                parent.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: SearchedMenuAdapter.CustomViewHolder, position: Int) {
        holder.menuPicture.setImageResource(menuTypeToIcons[menuList[position].menuType]!!)
        holder.menuTitle.text = menuList.get(position).menuTitle
        holder.startTime.text = menuList.get(position).startTime
        holder.endTime.text = menuList.get(position).endTime
        holder.menuDistance.text = menuList.get(position).menuDistance.toString()
        holder.quantity.text = menuList.get(position).quantity.toString()
        holder.discount.text = menuList.get(position).discount.toString()
        holder.discountedPrice.text = menuList.get(position).discountedPrice.toString()
        holder.originPrice.text = menuList.get(position).originPrice.toString()
        holder.originPrice.setPaintFlags(holder.originPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuPicture = itemView.findViewById<ImageView>(R.id.iv_menuPicture)
        val menuTitle = itemView.findViewById<TextView>(R.id.tv_menuTitle)
        val startTime = itemView.findViewById<TextView>(R.id.tv_startTime)
        val endTime = itemView.findViewById<TextView>(R.id.tv_endTime)
        val menuDistance = itemView.findViewById<TextView>(R.id.tv_menuDistance)
        val quantity = itemView.findViewById<TextView>(R.id.tv_quantity)
        val discount = itemView.findViewById<TextView>(R.id.tv_discount)
        val discountedPrice = itemView.findViewById<TextView>(R.id.tv_discountedPrice)
        val originPrice = itemView.findViewById<TextView>(R.id.tv_originMenuPrice)
    }
}