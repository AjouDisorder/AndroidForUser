package com.example.ttruserver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.models.SearchedMenuModel
import com.example.ttruserver2.models.SearchedRestaurantModel
import kotlinx.android.synthetic.main.activity_searched_menu_list.*
import kotlinx.android.synthetic.main.activity_searched_restaurant_list.*

class SearchedRestaurantListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_restaurant_list)

        val restaurantList
                = intent.getSerializableExtra("searchedRestaurantModelList") as ArrayList<SearchedRestaurantModel>

        if (restaurantList.size == 0){
            tv_invisMenuList3.visibility = View.VISIBLE
            iv_invisBox3.visibility = View.VISIBLE
            iv_invisTeardrop3.visibility = View.VISIBLE
        }else{
            rv_restaurantList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rv_restaurantList.setHasFixedSize(true)
            rv_restaurantList.adapter = SearchedRestaurantAdapter(restaurantList)
        }
    }
}
