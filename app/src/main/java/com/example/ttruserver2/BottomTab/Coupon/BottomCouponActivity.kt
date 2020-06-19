package com.example.ttruserver2.BottomTab.Coupon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ttruserver2.BottomTab.UserInfo.BottomMyInfoActivity
import com.example.ttruserver2.BottomTab.FavoriteRestaurant.BottomFavoriteRestaurantActivity
import com.example.ttruserver2.LogInActivity
import com.example.ttruserver2.MainActivity
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.UserData
import com.example.ttruserver2.models.TicketModel
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

        val coupon_list = arrayListOf<TicketModel>()

        val bottomCouponAdapter = BottomCouponListViewAdapter(this, coupon_list)

        iMyService.getTicketList(UserData.getOid().toString()).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@BottomCouponActivity, "$t", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()?.string()
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()){
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                    val location = jsonObject.getString("location")
                    val location_jsonobj = JSONObject(location)
                    val location_coordi = location_jsonobj.getString("coordinates")
                    val location_coordi_info = location_coordi.split("[",",","]")

                    val image = R.drawable.list7

                    val location_lat = location_coordi_info[2].toDouble()
                    val location_lng = location_coordi_info[1].toDouble()
                    val available = jsonObject.getBoolean("available")
                    val coupon_id = jsonObject.getString("_id")
                    val restaurant_id = jsonObject.getString("restaurant_id")
                    val address = jsonObject.getString("address")
                    val quantity = jsonObject.getString("quantity").toInt()
                    val totalPrice = jsonObject.getString("totalPrice").toInt()
                    val userName = jsonObject.getString("userName")
                    val menuName = jsonObject.getString("menuName")
                    val method = jsonObject.getString("method")
                    val value = jsonObject.getString("value")
                    val messageForBoss = jsonObject.getString("messageForBoss")
                    val restaurantTitle = jsonObject.getString("restaurantTitle")
                    val startDateString = jsonObject.getString("startDateObject")
                    val endDateString = jsonObject.getString("endDateObject")

                    coupon_list.add(0, TicketModel(image, location_lat, location_lng, available, coupon_id, restaurant_id,
                        address, quantity, totalPrice, userName, menuName, method, value, messageForBoss, restaurantTitle, startDateString, endDateString)
                    )
                }
                bottom_coupon_activity_listview.adapter = bottomCouponAdapter
            }

        })

        bottom_coupon_activity_listview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, BottomCouponInfoActivity::class.java)
            val selectedTicket = coupon_list[position]
            intent.putExtra("selectedTicket", selectedTicket)
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
            } else {
                val intent = Intent(this, BottomMyInfoActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
