package com.example.ttruserver2.BottomTab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ttruserver2.BottomTab.Coupon.BottomCouponActivity
import com.example.ttruserver2.BottomTab.FavoriteRestaurant.BottomFavoriteRestaurantActivity
import com.example.ttruserver2.LogInActivity
import com.example.ttruserver2.MainActivity
import com.example.ttruserver2.R
import com.example.ttruserver2.UserData
import kotlinx.android.synthetic.main.bottom.*

class BottomMyInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_my_info)


        //하단 탭
        bottom_tab_home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        bottom_tab_favorite_restaurant.setOnClickListener {
            if (UserData.getOid() == null){
                val loginIntent = Intent(this, LogInActivity::class.java)
                startActivity(loginIntent)
            }else{
                val intent = Intent(this, BottomFavoriteRestaurantActivity::class.java)
                startActivity(intent)
            }
        }
        bottom_tab_coupon.setOnClickListener {
            if (UserData.getOid() == null){
                val loginIntent = Intent(this, LogInActivity::class.java)
                startActivity(loginIntent)
            }else{
                val intent = Intent(this, BottomCouponActivity::class.java)
                startActivity(intent)
            }
        }
        bottom_tab_my_info.setOnClickListener {
            if(UserData.getOid() == null){
                val loginIntent = Intent(this, LogInActivity::class.java)
                startActivity(loginIntent)
            } else {
                val intent = Intent(this, BottomMyInfoActivity::class.java)
                startActivity(intent)
            }
        }

    }
}
