package com.example.ttruserver2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ttruserver2.models.SearchedRestaurantModel

class SearchedRestaurantAdapter (val restaurantList: ArrayList<SearchedRestaurantModel>) : RecyclerView.Adapter<SearchedRestaurantAdapter.CustomViewHolder>(){

    val restaurantTypeToIcons = hashMapOf("뷔페&샐러드" to R.drawable.store_buffet, "술집" to R.drawable.store_drink,
        "편의점" to R.drawable.store_convstore, "한식" to R.drawable.store_korean, "치킨" to R.drawable.store_chicken, "피자" to R.drawable.store_pizza,
        "족발&보쌈" to R.drawable.store_jokbal, "돈까스&일식&회" to R.drawable.store_japan, "양식&아시안" to R.drawable.store_american,
        "패스트푸드" to R.drawable.store_fastfood, "분식" to R.drawable.store_snack, "카페&디저트" to R.drawable.store_dessert,
        "찜&탕&찌개" to R.drawable.store_soup, "도시락" to R.drawable.store_dosirak, "중국집" to R.drawable.store_china)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedRestaurantAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item, parent, false)
        return SearchedRestaurantAdapter.CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val selectedRestaurant : SearchedRestaurantModel = restaurantList[curPos]
                val intent = Intent(parent.context, SearchedRestaurantDetailActivity::class.java)
                intent.putExtra("selectedRestaurant", selectedRestaurant)
                parent.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: SearchedRestaurantAdapter.CustomViewHolder, position: Int) {
        holder.restaurantPicture.setImageResource(restaurantTypeToIcons[restaurantList[position].type]!!)
        holder.restaurantType.text = restaurantList.get(position).type
        holder.restaurantTitle.text = restaurantList.get(position).title
        holder.restaurantGrade.text = (Math.round(restaurantList.get(position).grade*10)/10.0).toString()
        holder.restaurantDistance.text = restaurantList.get(position).distance.toString()
        if (!restaurantList.get(position).onSale){    //False면 "할인중" 안보이게
            holder.restaurantOnSale.visibility = View.INVISIBLE
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantPicture = itemView.findViewById<ImageView>(R.id.iv_restaurantPicture)
        val restaurantType = itemView.findViewById<TextView>(R.id.tv_restaurantType)
        val restaurantTitle = itemView.findViewById<TextView>(R.id.tv_restaurantTitle)
        val restaurantGrade = itemView.findViewById<TextView>(R.id.tv_restaurantGrade)
        val restaurantDistance = itemView.findViewById<TextView>(R.id.tv_restaurantDistance)
        val restaurantOnSale = itemView.findViewById<TextView>(R.id.tv_onSale)
    }

}