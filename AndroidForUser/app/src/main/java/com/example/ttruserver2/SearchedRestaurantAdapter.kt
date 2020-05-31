package com.example.ttruserver2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ttruserver2.models.SearchedRestaurantModel

class SearchedRestaurantAdapter (val restaurantList: ArrayList<SearchedRestaurantModel>) : RecyclerView.Adapter<SearchedRestaurantAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedRestaurantAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item, parent, false)
        return SearchedRestaurantAdapter.CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val restaurant : SearchedRestaurantModel = restaurantList.get(curPos)
                Toast.makeText(parent.context, "이름 : ${restaurant.restaurantTitle}", Toast.LENGTH_SHORT).show()
                val intent = Intent(parent.context, SearchedMenuDetailActivity::class.java)
                parent.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: SearchedRestaurantAdapter.CustomViewHolder, position: Int) {
        holder.restaurantPicture.setImageResource(restaurantList.get(position).restaurantPicture)
        holder.restaurantTitle.text = restaurantList.get(position).restaurantTitle
        holder.restaurantGrade.text = restaurantList.get(position).restaurantGrade.toString()
        holder.restaurantDistance.text = restaurantList.get(position).restaurantDistance.toString()
        holder.restaurantType.text = restaurantList.get(position).restaurantType
        if (!restaurantList.get(position).restaurantOnSale){    //False면 "할인중" 안보이게
            holder.restaurantOnSale.visibility = View.INVISIBLE
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantPicture = itemView.findViewById<ImageView>(R.id.iv_restaurantPicture)
        val restaurantTitle = itemView.findViewById<TextView>(R.id.tv_restaurantTitle)
        val restaurantGrade = itemView.findViewById<TextView>(R.id.tv_restaurantGrade)
        val restaurantDistance = itemView.findViewById<TextView>(R.id.tv_restaurantDistance)
        val restaurantType = itemView.findViewById<TextView>(R.id.tv_restaurantType)
        val restaurantOnSale = itemView.findViewById<TextView>(R.id.tv_onSale)
    }

}