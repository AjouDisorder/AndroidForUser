package com.example.ttruserver2;

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.detailRestaurant.InfoFragment
import com.example.ttruserver2.detailRestaurant.MenuFragment
import com.example.ttruserver2.detailRestaurant.ReviewFragment
import com.example.ttruserver2.models.SearchedRestaurantModel
import com.google.firebase.messaging.FirebaseMessaging

import kotlinx.android.synthetic.main.activity_searched_restaurant_detail.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchedRestaurantDetailActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService
    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_restaurant_detail)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        val selectedRestaurant = intent.getSerializableExtra("selectedRestaurant") as SearchedRestaurantModel

        //menu fragment init
        val fragmentMenu = MenuFragment()
        val fragmentReview = ReviewFragment()
        val restaurantBundle = Bundle()
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
        tv_rating.text = (Math.round(selectedRestaurant.grade*10)/10.0).toString();
        ratingBar.rating = (Math.round(selectedRestaurant.grade*10)/10.0).toFloat()

        var favoriteCount = selectedRestaurant.favoriteCount
        iMyService.getRestaurantDetail(selectedRestaurant.restaurantOid).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SearchedRestaurantDetailActivity, "Fail : $t", Toast.LENGTH_SHORT).show() }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()?.string()
                val jsonObject = JSONObject(result)

                favoriteCount = jsonObject.getInt("favoriteCount")
                tv_favoriteCount.text = favoriteCount.toString()
            }
        })

        tv_distance.text = (selectedRestaurant.distance).toString()
        btn_dial.setOnClickListener{
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${selectedRestaurant.phone}")
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
        }

        //Favorite Services
        var isFavorite : Boolean = false
        iv_isFavorite.setImageResource(R.drawable.emptyheart)
        if (UserData.getOid() != null){
            iMyService.getFavoriteList(UserData.getOid()).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@SearchedRestaurantDetailActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val result = response.body()?.string()
                    val jsonArray = JSONArray(result)

                    for (i in 0.until(jsonArray.length())){
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        if(selectedRestaurant.restaurantOid == jsonObject.getString("_id")){
                            isFavorite = true
                            iv_isFavorite.setImageResource(R.drawable.fillheart)
                            break
                        }
                    }
                }
            })
        }
        //favorite btn
        btn_setFavorite.setOnClickListener{
            if (UserData.getOid() == null){
                intent = Intent(this, LogInActivity::class.java)
                intent.putExtra("fromRestaurantDetail", selectedRestaurant)
                startActivity(intent)
                finish()
            }else{
                if (isFavorite){    //즐겨찾기 해제
                    iMyService.deleteRestaurantInFavoriteList(UserData.getOid(), selectedRestaurant.restaurantOid).enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@SearchedRestaurantDetailActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            isFavorite = false
                            favoriteCount -= 1
                            tv_favoriteCount.text = favoriteCount.toString()
                            iv_isFavorite.setImageResource(R.drawable.emptyheart)
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(selectedRestaurant.restaurantOid)
                        }
                    })
                }else{  //즐겨찾기 추가
                    isFavorite = true
                    iMyService.addRestaurantToFavoriteList(UserData.getOid(), selectedRestaurant.restaurantOid).enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@SearchedRestaurantDetailActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            favoriteCount += 1
                            tv_favoriteCount.text = favoriteCount.toString()
                            iv_isFavorite.setImageResource(R.drawable.fillheart)
                            FirebaseMessaging.getInstance().subscribeToTopic(selectedRestaurant.restaurantOid);
                        }
                    })
                }
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
                fragmentMenu.arguments = restaurantBundle
                transaction.replace(R.id.fl_detailMenu, fragmentMenu)
                transaction.commit();
                return false
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
                return false
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
                fragmentReview.arguments = restaurantBundle
                transaction.replace(R.id.fl_detailMenu, fragmentReview)
                transaction.commit()
                return false
            }
        })
        button.setOnClickListener {
            val location = Uri.parse("kakaomap://route?sp="+UserData.getLat()+","+UserData.getLng()+"&ep="+selectedRestaurant.lat+","+selectedRestaurant.lng+"&by=FOOT")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)

            val activities: List<ResolveInfo> = packageManager.queryIntentActivities(mapIntent, 0)
            val isIntentSafe: Boolean = activities.isNotEmpty()

            if (isIntentSafe) {
                startActivity(mapIntent)
            }
        }
    }
}
