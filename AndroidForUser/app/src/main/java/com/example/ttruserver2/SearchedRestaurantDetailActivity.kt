package com.example.ttruserver2;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.net.Uri
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import com.example.ttruserver2.detailRestaurant.InfoFragment
import com.example.ttruserver2.detailRestaurant.MenuFragment
import com.example.ttruserver2.detailRestaurant.ReviewFragment
import com.example.ttruserver2.models.SearchedRestaurantModel

import kotlinx.android.synthetic.main.activity_searched_reataurant_detail.*

class SearchedRestaurantDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_reataurant_detail)

        val selectedRestaurant = intent.getSerializableExtra("selectedRestaurant") as SearchedRestaurantModel

        //menu fragment init
        val fragmentMenu = MenuFragment()
        var restaurantBundle = Bundle()
        restaurantBundle.putString("restaurantOid", selectedRestaurant.restaurantOid)
        restaurantBundle.putDouble("restaurantDistance", selectedRestaurant.distance)
        restaurantBundle.putParcelableArrayList("originMenuList", selectedRestaurant.originMenuList)
        fragmentMenu.arguments = restaurantBundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_detailMenu, fragmentMenu)
            .commit()
        //restaurant data assign
        tv_restaurantTitle.text = selectedRestaurant.title
        var infoBundle = Bundle()
        infoBundle.putString("description", selectedRestaurant.description)
        infoBundle.putString("address", selectedRestaurant.address)
        tv_rating.text = (selectedRestaurant.grade).toString();
        ratingBar.rating = selectedRestaurant.grade.toFloat()
        tv_favoriteCount.text = (selectedRestaurant.favoriteCount).toString();
        var favoriteCount = selectedRestaurant.favoriteCount
        tv_distance.text = (selectedRestaurant.distance).toString()
        btn_dial.setOnClickListener{
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${selectedRestaurant.phone}")
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
        }
        //todo 즐겨찾기 추가 되어있는지
        var isFavorite : Boolean = false
        if (isFavorite){
            iv_isFavorite.setImageResource(R.drawable.fillheart)
        }else{
            iv_isFavorite.setImageResource(R.drawable.emptyheart)
        }
        //todo 즐겨찾기 버튼
        btn_setFavorite.setOnClickListener{
            if (isFavorite){    //즐겨찾기 해제
                isFavorite = false
                favoriteCount -= 1
                tv_favoriteCount.text = favoriteCount.toString()
                iv_isFavorite.setImageResource(R.drawable.emptyheart)
            }else{  //즐겨찾기 추가
                isFavorite = true
                favoriteCount += 1
                tv_favoriteCount.text = favoriteCount.toString()
                iv_isFavorite.setImageResource(R.drawable.fillheart)
            }
        }

        //아래는 버튼(메뉴, 정보, 리뷰) 처리
        btn_detailMenu_menu.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, motionEvent: MotionEvent?) :Boolean{
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btn_detailMenu_menu.setBackgroundColor(Color.TRANSPARENT)
                    }
                    MotionEvent.ACTION_UP -> {
                        btn_detailMenu_menu.setBackgroundColor(Color.rgb(142, 137, 137));
                        btn_detailMenu_info.setBackgroundColor(Color.rgb(205, 199, 199));
                        btn_detailMenu_review.setBackgroundColor(Color.rgb(205, 199, 199));
                    }
                }

                val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
                //val fragmentMenu = MenuFragment()
                fragmentMenu.arguments = restaurantBundle
                transaction.replace(R.id.fl_detailMenu, fragmentMenu)
                transaction.commit();
                return false;
            }
        })
        btn_detailMenu_info.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, motionEvent: MotionEvent?) :Boolean{
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btn_detailMenu_info.setBackgroundColor(Color.TRANSPARENT);
                    }
                    MotionEvent.ACTION_UP -> {
                        btn_detailMenu_info.setBackgroundColor(Color.rgb(142, 137, 137));
                        btn_detailMenu_menu.setBackgroundColor(Color.rgb(205, 199, 199));
                        btn_detailMenu_review.setBackgroundColor(Color.rgb(205, 199, 199));
                    }
                }

                val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
                val fragmentInfo = InfoFragment()
                fragmentInfo.arguments = infoBundle
                transaction.replace(R.id.fl_detailMenu, fragmentInfo)
                transaction.commit();
                return false;
            }
        })
        btn_detailMenu_review.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, motionEvent: MotionEvent?) :Boolean{
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btn_detailMenu_review.setBackgroundColor(Color.TRANSPARENT)
                    }
                    MotionEvent.ACTION_UP -> {
                        btn_detailMenu_review.setBackgroundColor(Color.rgb(142, 137, 137));
                        btn_detailMenu_menu.setBackgroundColor(Color.rgb(205, 199, 199));
                        btn_detailMenu_info.setBackgroundColor(Color.rgb(205, 199, 199));
                    }
                }

                val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
                val fragmentReview = ReviewFragment()
                transaction.replace(R.id.fl_detailMenu, fragmentReview)
                transaction.commit();
                return false;
            }
        })
    }
}
