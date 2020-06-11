package com.example.ttruserver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.OriginMenuModel
import com.example.ttruserver2.models.SearchedMenuModel
import com.example.ttruserver2.models.SearchedRestaurantModel
import kotlinx.android.synthetic.main.activity_search_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject

class SearchBarActivity : AppCompatActivity() {
    lateinit var iMyService: IMyService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_bar)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        //recycler view
        val selectedIconType = intent.getIntExtra("selectedIconType", 0)
        rv_searchLog.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv_searchLog.setHasFixedSize(true)
        if (selectedIconType == 0){
            et_searchBar.hint = "메뉴 검색"
            rv_searchLog.adapter = SearchBarAdapter(UserData.getSearchedMenuLog(), selectedIconType)
        }else{
            et_searchBar.hint = "가게 검색"
            rv_searchLog.adapter = SearchBarAdapter(UserData.getSearchedRestaurantLog(), selectedIconType)
        }

        //findBySearchBar
        iv_searchBtn.setOnClickListener{
            if (et_searchBar.text.toString().length < 2){
                Toast.makeText(this@SearchBarActivity, "최소 두글지 이상 입력하세요", Toast.LENGTH_SHORT).show()
            }
            else if (selectedIconType == 0){
                iMyService.getMenuBySearchBar(et_searchBar.text.toString(), UserData.getLat(), UserData.getLng()).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@SearchBarActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val result = response.body()?.string()
                        val jsonArray = JSONArray(result)
                        val searchedMenuModelList = arrayListOf<SearchedMenuModel>()

                        for (i in 0.until(jsonArray.length())){
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                            val _id = jsonObject.getString("_id")
                            val restaurantTitle = jsonObject.getString("restaurantTitle")
                            val restaurantOid = jsonObject.getJSONObject("originMenu").getString("restaurant_id")
                            val menuType = jsonObject.getString("type")
                            val title = jsonObject.getString("title")
                            val startTime = jsonObject.getString("startDateObject").substring(11, 16)
                            val endTime = jsonObject.getString("endDateObject").substring(11, 16)
                            val distance = Math.round(jsonObject.getDouble("distance")/100.0)/10.0
                            val quantity = jsonObject.getInt("quantity")
                            val discount = jsonObject.getInt("discount")
                            val originPrice = jsonObject.getJSONObject("originMenu").getInt("originPrice")
                            val discountedPrice = originPrice - (originPrice * discount / 100)
                            val method = jsonObject.getString("method")

                            searchedMenuModelList.add(SearchedMenuModel(_id, restaurantTitle, restaurantOid, menuType, title,
                                startTime, endTime, distance, quantity, discount, discountedPrice, originPrice, method))
                        }
                        UserData.addSearchedMenuLog(et_searchBar.text.toString())
                        val intent = Intent(this@SearchBarActivity, SearchedMenuListActivity::class.java)
                        intent.putExtra("searchedMenuModelList", searchedMenuModelList)
                        startActivity(intent)
                        finish()
                    }
                })
            }else if(selectedIconType == 1){
                iMyService.getRestaurantBySearchBar(et_searchBar.text.toString(), UserData.getLat(), UserData.getLng()).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@SearchBarActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val result = response.body()?.string()
                        val jsonArray = JSONArray(result)
                        val searchedRestaurantModelList = arrayListOf<SearchedRestaurantModel>()

                        for (i in 0.until(jsonArray.length())){
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                            val _id = jsonObject.getString("_id")
                            val storeType = jsonObject.getString("type")
                            val title = jsonObject.getString("title")
                            val grade = jsonObject.getDouble("avrGrade")
                            val distance = Math.round(jsonObject.getDouble("distance")/100.0)/10.0
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

                            searchedRestaurantModelList.add(SearchedRestaurantModel(_id, storeType, title, grade,
                                distance, onSale, favoriteCount, description, address, phone, originMenuList,
                                lng as Double, lat as Double))
                        }
                        UserData.addSearchedRestaurantLog(et_searchBar.text.toString())
                        val intent = Intent(this@SearchBarActivity, SearchedRestaurantListActivity::class.java)
                        intent.putExtra("searchedRestaurantModelList", searchedRestaurantModelList)
                        startActivity(intent)
                        finish()
                    }
                })
            }
        }
    }
}