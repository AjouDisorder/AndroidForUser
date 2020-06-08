package com.example.ttruserver2.BottomTab.Coupon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ttruserver2.BottomTab.*
import com.example.ttruserver2.BottomTab.FavoriteRestaurant.BottomFavoriteRestaurantActivity
import com.example.ttruserver2.MainActivity
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.UserData
import com.example.ttruserver2.models.BottomCouponContentsListModel
import kotlinx.android.synthetic.main.activity_bottom_coupon.*
import kotlinx.android.synthetic.main.bottom.*

import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomCouponActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_coupon)

        //init retrofit api
        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        val coupon_list = arrayListOf<BottomCouponContentsListModel>()

        val bottomCouponAdapter = BottomCouponListViewAdapter(this, coupon_list)

        iMyService.getTicketList(UserData.getOid().toString()).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("getTicketList_onFailure", t?.message.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var result = response.body()?.string()
                Log.d("@@ticketlist: ", result)
                var jsonArray = JSONArray(result)
                for (i in 0..(jsonArray.length()-1)){
                    var jsonObject: JSONObject = jsonArray.getJSONObject(i)

                    var location = jsonObject.getString("location")
                    var location_jsonobj = JSONObject(location)
                    var location_coordi = location_jsonobj.getString("coordinates")
                    var location_coordi_info = location_coordi.split("[",",","]")

                    var image = R.drawable.list7
                    Log.d("location(lat, lng): ", location_coordi_info[2] +','+ location_coordi_info[1])

                    var location_lat = location_coordi_info[2].toDouble()
                    var location_lng = location_coordi_info[1].toDouble()
                    var available = jsonObject.getString("available")
                    var coupon_id = jsonObject.getString("_id")
                    var restaurant_id = jsonObject.getString("restaurant_id")
                    var address = jsonObject.getString("address")
                    var quantity = jsonObject.getString("quantity").toInt()
                    var totalPrice = jsonObject.getString("totalPrice").toInt()
                    var userName = jsonObject.getString("userName")
                    var menuName = jsonObject.getString("menuName")
                    var method = jsonObject.getString("method")
                    var value = jsonObject.getString("value")
                    var messageForBoss = jsonObject.getString("messageForBoss")
                    var restaurantTitle = jsonObject.getString("restaurantTitle")
                    Log.d("messageboss_contents", messageForBoss)

                    coupon_list.add(BottomCouponContentsListModel(image, location_lat, location_lng, available, coupon_id, restaurant_id,
                        address, quantity, totalPrice, userName, menuName, method, value, messageForBoss, restaurantTitle)
                    )
                }
                bottom_coupon_activity_listview.adapter = bottomCouponAdapter
            }

        })

        bottom_coupon_activity_listview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, BottomCouponInfoActivity::class.java)
            //            val item = bottomCouponAdapter.getItem(position)
            var dataCouponList = coupon_list[position]
            Log.d("dataCouponList", dataCouponList.toString())

            intent.putExtra("location_lat", dataCouponList.location_lat.toString())
            intent.putExtra("location_lng", dataCouponList.location_lng.toString())
            intent.putExtra("available", dataCouponList.available.toString())
            intent.putExtra("coupon_id", dataCouponList.coupon_id.toString())
            intent.putExtra("restaurant_id", dataCouponList.restaurant_id.toString())
            intent.putExtra("address", dataCouponList.address.toString())
            intent.putExtra("quantity", dataCouponList.quantity.toString())
            intent.putExtra("totalPrice", dataCouponList.totalPrice.toString())
            intent.putExtra("userName", dataCouponList.userName.toString())
            intent.putExtra("menuName", dataCouponList.menuName.toString())
            intent.putExtra("method", dataCouponList.method.toString())
            intent.putExtra("value", dataCouponList.value.toString())
            intent.putExtra("messageForBoss", dataCouponList.messageForBoss.toString())
            intent.putExtra("restaurantTitle",dataCouponList.restaurantTitle.toString())
            startActivity(intent)
        }

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
