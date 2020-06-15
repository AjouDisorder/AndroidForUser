package com.example.ttruserver2.BottomTab.FavoriteRestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ttruserver2.BottomTab.Coupon.BottomCouponActivity
import com.example.ttruserver2.LogInActivity
import com.example.ttruserver2.MainActivity
import com.example.ttruserver2.R
import com.example.ttruserver2.UserData
import kotlinx.android.synthetic.main.activity_bottom_favorite_restaurant.*
import kotlinx.android.synthetic.main.bottom.*

class BottomFavoriteRestaurantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_favorite_restaurant)

        val favo_restau_list_array = arrayListOf<BottomFavoriteRestaurantContentListModel>(
            BottomFavoriteRestaurantContentListModel(
                R.drawable.list1,
                "안성찜닭",
                10000,
                "내찜닭"
            ),
            BottomFavoriteRestaurantContentListModel(
                R.drawable.list2,
                "제육볶음",
                6000,
                "미스터쉐프"
            ),
            BottomFavoriteRestaurantContentListModel(
                R.drawable.list3,
                "치킨",
                18000,
                "비비큐"
            ),
            BottomFavoriteRestaurantContentListModel(
                R.drawable.list4,
                "우동",
                4500,
                "역전우동"
            ),
            BottomFavoriteRestaurantContentListModel(
                R.drawable.list5,
                "피자",
                23000,
                "미스터피자"
            ),
            BottomFavoriteRestaurantContentListModel(
                R.drawable.list6,
                "도넛츠",
                6000,
                "크리스피도넛"
            ),
            BottomFavoriteRestaurantContentListModel(
                R.drawable.list7,
                "짜장면",
                7000,
                "홍콩반점"
            )
        )
        val bottomFavoriteRestaurantListViewAdapter = BottomFavoriteRestaurantListViewAdapter(this, favo_restau_list_array)
        bottom_favo_restau_info_activity_listview.adapter = bottomFavoriteRestaurantListViewAdapter

        bottom_favo_restau_info_activity_listview.setOnItemClickListener { parent, view, position, id ->
            val item = bottomFavoriteRestaurantListViewAdapter.getItem(position)
            Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show()
            val intent = Intent(this, BottomFavoriteRestaurantInfoActivity::class.java)
            intent.putExtra("fav_restaurant_info", item.toString())
            startActivity(intent)
        }

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
                finish()
            }
        }
        bottom_tab_coupon.setOnClickListener {
            if (UserData.getOid() == null){
                val loginIntent = Intent(this, LogInActivity::class.java)
                startActivity(loginIntent)
            }else{
                val intent = Intent(this, BottomCouponActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        bottom_tab_my_info.setOnClickListener {
            if(UserData.getOid() == null){
                val loginIntent = Intent(this, LogInActivity::class.java)
                startActivity(loginIntent)
            }/* else {
                val intent = Intent(this, BottomMyInfoActivity::class.java)
                startActivity(intent)
            }*/
        }

    }
}
