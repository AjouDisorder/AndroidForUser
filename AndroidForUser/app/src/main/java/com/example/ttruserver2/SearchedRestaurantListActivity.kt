package com.example.ttruserver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.models.SearchedMenuModel
import com.example.ttruserver2.models.SearchedRestaurantModel
import kotlinx.android.synthetic.main.activity_searched_restaurant_list.*

class SearchedRestaurantListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_restaurant_list)

        val restaurantList
                = intent.getSerializableExtra("searchedRestaurantModelList") as ArrayList<SearchedRestaurantModel>

        rv_restaurantList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_restaurantList.setHasFixedSize(true)
        rv_restaurantList.adapter = SearchedRestaurantAdapter(restaurantList)
    }
}
