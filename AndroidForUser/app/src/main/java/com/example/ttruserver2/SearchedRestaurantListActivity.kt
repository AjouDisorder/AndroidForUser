package com.example.ttruserver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.models.SearchedRestaurantModel
import kotlinx.android.synthetic.main.activity_searched_restaurant_list.*

class SearchedRestaurantListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_restaurant_list)

        val restaurantList = arrayListOf(
            SearchedRestaurantModel(
                "tmp", "술집", "김형건 술집", 4.8,
                1.5,true
            ),
            SearchedRestaurantModel(
                "tmp","패스트푸드","맥도날드 수원 아주대점",
                3.9, 0.7,false
            )
        )

        rv_restaurantList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_restaurantList.setHasFixedSize(true)
        rv_restaurantList.adapter = SearchedRestaurantAdapter(restaurantList)
    }
}
