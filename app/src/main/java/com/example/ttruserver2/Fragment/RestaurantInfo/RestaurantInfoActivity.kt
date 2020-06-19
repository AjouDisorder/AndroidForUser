package com.example.ttruserver2.Fragment.RestaurantInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ttruserver2.R
import kotlinx.android.synthetic.main.activity_restaurant_info.*

class RestaurantInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_info)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_area, MenuFragment())
            .commit()

        restaurant_menu.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_area, MenuFragment())
                .commit()
        }
        restaurant_info.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_area, InfoFragment())
                .commit()
        }
        restaurant_review.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_area, ReviewFragment())
                .commit()
        }
    }//
}
