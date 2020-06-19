package com.example.ttruserver2.BottomTab.FavoriteRestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ttruserver2.BottomTab.Coupon.BottomCouponActivity
import com.example.ttruserver2.LogInActivity
import com.example.ttruserver2.MainActivity
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.UserData
import kotlinx.android.synthetic.main.activity_bottom_favorite_restaurant.*
import kotlinx.android.synthetic.main.bottom.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomFavoriteRestaurantActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_favorite_restaurant)

        //@@@@
        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)
//@@@@
        val favo_restau_list_array = arrayListOf<BottomFavoriteRestaurantContentListModel>()
        val bottomFavoriteRestaurantListViewAdapter = BottomFavoriteRestaurantListViewAdapter(this, favo_restau_list_array)


        iMyService.getFavoriteList(UserData.getOid().toString()).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@BottomFavoriteRestaurantActivity, "getFavoriteList: Fail!", Toast.LENGTH_SHORT).show()
                Log.d("getFavRes", "getFavRes_Fail")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(this@BottomFavoriteRestaurantActivity, "getFavoriteList: Success!", Toast.LENGTH_SHORT).show()
                Log.d("getFavRes", "getFavRes_Success")
                val result = response?.body()?.string()
                Log.d("deleteResFav:", result.toString())
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()){
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)

//                    val location = jsonObject.getString("location")
//                    val location_jsonobj = JSONObject(location)
//                    val location_coordi = location_jsonobj.getString("coordinates")
//                    val location_coordi_info = location_coordi.split("[",",","]")

                    val image = R.drawable.list7

//                    val location_lat = location_coordi_info[2].toDouble()
//                    val location_lng = location_coordi_info[1].toDouble()
//                    val available = jsonObject.getBoolean("available")
//                    val coupon_id = jsonObject.getString("_id")
                    val restaurant_id = jsonObject.getString("_id")
                    val address = jsonObject.getString("address")
                    val avrGrade = jsonObject.getString("avrGrade")
                    val menuidList = jsonObject.getString("menuidList")
                    var saleOn = " "
                    if (menuidList != null){
                        var saleOn = "할인중"
                    }


//                    val quantity = jsonObject.getString("quantity").toInt()
//                    val totalPrice = jsonObject.getString("totalPrice").toInt()
//                    val userName = jsonObject.getString("userName")
//                    val menuName = jsonObject.getString("menuName")
//                    val method = jsonObject.getString("method")
//                    val value = jsonObject.getString("value")
//                    val messageForBoss = jsonObject.getString("messageForBoss")
//                    val restaurantTitle = jsonObject.getString("restaurantTitle")
//                    val startDateString = jsonObject.getString("startDateObject")
//                    val endDateString = jsonObject.getString("endDateObject")
                    val title = jsonObject.getString("title")
                    val type = jsonObject.getString("type")

                    favo_restau_list_array.add(0, BottomFavoriteRestaurantContentListModel(image, title, address, type, avrGrade, saleOn))
                }
                bottom_favo_restau_info_activity_listview.adapter = bottomFavoriteRestaurantListViewAdapter
            }

        })


//        val favo_restau_list_array = arrayListOf<BottomFavoriteRestaurantContentListModel>(
//            BottomFavoriteRestaurantContentListModel(
//                R.drawable.list1,
//                "안성찜닭",
//                10000,
//                "내찜닭"
//            ),
//            BottomFavoriteRestaurantContentListModel(
//                R.drawable.list2,
//                "제육볶음",
//                6000,
//                "미스터쉐프"
//            ),
//            BottomFavoriteRestaurantContentListModel(
//                R.drawable.list3,
//                "치킨",
//                18000,
//                "비비큐"
//            ),
//            BottomFavoriteRestaurantContentListModel(
//                R.drawable.list4,
//                "우동",
//                4500,
//                "역전우동"
//            ),
//            BottomFavoriteRestaurantContentListModel(
//                R.drawable.list5,
//                "피자",
//                23000,
//                "미스터피자"
//            ),
//            BottomFavoriteRestaurantContentListModel(
//                R.drawable.list6,
//                "도넛츠",
//                6000,
//                "크리스피도넛"
//            ),
//            BottomFavoriteRestaurantContentListModel(
//                R.drawable.list7,
//                "짜장면",
//                7000,
//                "홍콩반점"
//            )
//        )
//        val bottomFavoriteRestaurantListViewAdapter = BottomFavoriteRestaurantListViewAdapter(this, favo_restau_list_array)
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
