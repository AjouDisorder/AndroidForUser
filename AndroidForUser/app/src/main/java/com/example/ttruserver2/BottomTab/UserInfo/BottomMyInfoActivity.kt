package com.example.ttruserver2.BottomTab.UserInfo

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ttruserver2.*
import com.example.ttruserver2.BottomTab.Coupon.BottomCouponActivity
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.OriginMenuModel
import com.example.ttruserver2.models.SearchedRestaurantModel
import kotlinx.android.synthetic.main.activity_bottom_my_info.*
import kotlinx.android.synthetic.main.bottom.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomMyInfoActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_my_info)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        tv_userName.text = "이름 :  ${UserData.getName()}"
        tv_userSex.text = "성별 :  ${UserData.getSex()}"
        tv_userDateOfBirth.text = "생년월일 :  ${UserData.getDateOfBirth()}"
        tv_userPhone.text = "휴대폰번호 :  ${UserData.getPhone()}"

        btn_updateInfo.setOnClickListener {
            val intent = Intent(this, UpdateInfoActivity::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener {
            UserData.setOid(null)
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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
                iMyService.getFavoriteList(UserData.getOid().toString()).enqueue(object :
                    Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@BottomMyInfoActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val favoriteRestaurantModelList = arrayListOf<SearchedRestaurantModel>()
                        val result = response.body()?.string()
                        val jsonArray = JSONArray(result)

                        for (i in 0.until(jsonArray.length())){
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                            val _id = jsonObject.getString("_id")
                            val type = jsonObject.getString("type")
                            val title = jsonObject.getString("title")
                            val grade = jsonObject.getDouble("avrGrade")

                            var onSale = true
                            if (jsonObject.getJSONArray("menuidList").length() == 0){
                                onSale = false
                            }
                            val favoriteCount = jsonObject.getInt("favoriteCount")
                            val description = jsonObject.getString("description")
                            val address = jsonObject.getString("address")
                            val phone = jsonObject.getString("phone")

                            var originMenuList =  arrayListOf<OriginMenuModel>()
                            val originMenuJson = jsonObject.getJSONArray("originMenuList")
                            for(i in 0.until(originMenuJson.length())){
                                val originMenu: JSONObject = originMenuJson.getJSONObject(i)
                                val originMenuTitle = originMenu.getString("title")
                                val originMenuPrice = originMenu.getInt("originPrice")
                                originMenuList.add(OriginMenuModel(originMenuTitle, originMenuPrice))
                            }

                            val lng = jsonObject.getJSONObject("location").getJSONArray("coordinates").get(0)
                            val lat= jsonObject.getJSONObject("location").getJSONArray("coordinates").get(1)

                            val userLocation = Location("userPoint")
                            userLocation.latitude = UserData.getLat() as Double
                            userLocation.longitude = UserData.getLng() as Double
                            val restaurantLocation = Location("restaurantPoint")
                            restaurantLocation.latitude = lat as Double
                            restaurantLocation.longitude = lng as Double
                            val distance : Double = Math.round((userLocation.distanceTo(restaurantLocation)).toDouble()/100.0)/10.0

                            if(_id != null){
                                favoriteRestaurantModelList.add(
                                    SearchedRestaurantModel(_id, type, title, grade,
                                        distance, onSale, favoriteCount, description, address, phone, originMenuList,
                                        lng, lat)
                                )
                            }
                        }
                        val intent = Intent(this@BottomMyInfoActivity, SearchedRestaurantListActivity::class.java)
                        intent.putExtra("searchedRestaurantModelList", favoriteRestaurantModelList)
                        startActivity(intent)
                    }
                })
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
    }
}