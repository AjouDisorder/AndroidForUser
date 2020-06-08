package com.example.ttruserver2.BottomTab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ttruserver2.BottomTab.Coupon.BottomCouponActivity
import com.example.ttruserver2.BottomTab.FavoriteRestaurant.BottomFavoriteRestaurantActivity
import com.example.ttruserver2.MainActivity
import com.example.ttruserver2.R
import kotlinx.android.synthetic.main.bottom.*

class BottomMyInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_my_info)

        bottom_tab_home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        bottom_tab_favorite_restaurant.setOnClickListener {
            val intent = Intent(this, BottomFavoriteRestaurantActivity::class.java)
            startActivity(intent)
        }

        bottom_tab_coupon.setOnClickListener {
            val intent = Intent(this, BottomCouponActivity::class.java)
            startActivity(intent)
        }
        bottom_tab_my_info.setOnClickListener {
            val intent = Intent(this, BottomMyInfoActivity::class.java)
            startActivity(intent)
        }

    }
}
