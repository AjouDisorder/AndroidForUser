package com.example.ttruserver2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.SearchedMenuModel
import kotlinx.android.synthetic.main.activity_get_menu_by_time.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class GetMenuByTimeActivity : AppCompatActivity() {
    lateinit var iMyService: IMyService
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_menu_by_time)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        //캘린더
        val cal: Calendar = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val c: Calendar = Calendar.getInstance()
        val hh=c.get(Calendar.HOUR_OF_DAY)
        val mm=c.get(Calendar.MINUTE)

        tv_now.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                tv_now.setText(""+year+"-"+(month+1)+"-" +dayOfMonth)
            }, y, m, d)
            dpd.show()
        }
        tv_currentTime.setOnClickListener {
            val timePickerDialog: TimePickerDialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                tv_currentTime.setText( ""+hourOfDay + ":" + minute);
            },hh,mm,true)
            timePickerDialog.show()
        }
        val now = LocalDate.now()
        val currentTime = LocalTime.now()
        tv_now.text = now.toString()
        tv_currentTime.text = currentTime.toString().substring(0, 5)
        var searchedMenuModelList = arrayListOf<SearchedMenuModel>()
        iMyService.getMenuByTime(now.year, now.monthValue, now.dayOfMonth, currentTime.hour, currentTime.minute,
            UserData.getLat(), UserData.getLng())
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@GetMenuByTimeActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val result = response.body()?.string()
                    val jsonArray = JSONArray(result)

                    for (i in 0.until(jsonArray.length())){
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                        val _id = jsonObject.getString("_id")
                        val restaurantTitle = jsonObject.getString("restaurantTitle")
                        val restaurantOid = jsonObject.getJSONObject("originMenu").getString("restaurant_id")
                        val type = jsonObject.getString("type")
                        val title = jsonObject.getString("title")
                        val startTime = jsonObject.getString("startDateObject").substring(11, 16)
                        val endTime = jsonObject.getString("endDateObject").substring(11, 16)
                        val distance = Math.round(jsonObject.getDouble("distance")/100.0)/10.0
                        val quantity = jsonObject.getInt("quantity")
                        val discount = jsonObject.getInt("discount")
                        val originPrice = jsonObject.getJSONObject("originMenu").getInt("originPrice")
                        val discountedPrice = originPrice - (originPrice * discount / 100)
                        val method = jsonObject.getString("method")

                        searchedMenuModelList.add(SearchedMenuModel(_id, restaurantTitle, restaurantOid, type, title,
                            startTime, endTime, distance, quantity, discount, discountedPrice, originPrice, method))

                        rv_getMenuByTime.layoutManager = LinearLayoutManager(this@GetMenuByTimeActivity, LinearLayoutManager.VERTICAL, false)
                        rv_getMenuByTime.setHasFixedSize(true)
                        rv_getMenuByTime.adapter = SearchedMenuAdapter(searchedMenuModelList)
                    }
                }
            })

        time_search_refresh.setOnClickListener {
            val date_data = tv_now.text.toString().split("-")
            val time_data = tv_currentTime.text.toString().split(":")
            val _year = date_data[0]
            val _month = date_data[1]
            val _day = date_data[2]
            val _hour = time_data[0]
            val _minute = time_data[1]
            searchedMenuModelList.clear()
            iMyService.getMenuByTime(_year.toInt(), _month.toInt(), _day.toInt(), _hour.toInt(), _minute.toInt(), UserData.getLat(), UserData.getLng())
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@GetMenuByTimeActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val result = response.body()?.string()
                        val jsonArray = JSONArray(result)

                        for (i in 0.until(jsonArray.length())){
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                            val _id = jsonObject.getString("_id")
                            val restaurantTitle = jsonObject.getString("restaurantTitle")
                            val restaurantOid = jsonObject.getJSONObject("originMenu").getString("restaurant_id")
                            val type = jsonObject.getString("type")
                            val title = jsonObject.getString("title")
                            val startTime = jsonObject.getString("startDateObject").substring(11, 16)
                            val endTime = jsonObject.getString("endDateObject").substring(11, 16)
                            val distance = Math.round(jsonObject.getDouble("distance")/100.0)/10.0
                            val quantity = jsonObject.getInt("quantity")
                            val discount = jsonObject.getInt("discount")
                            val originPrice = jsonObject.getJSONObject("originMenu").getInt("originPrice")
                            val discountedPrice = originPrice - (originPrice * discount / 100)
                            val method = jsonObject.getString("method")

                            searchedMenuModelList.add(SearchedMenuModel(_id, restaurantTitle, restaurantOid, type, title,
                                startTime, endTime, distance, quantity, discount, discountedPrice, originPrice, method))

                            rv_getMenuByTime.layoutManager = LinearLayoutManager(this@GetMenuByTimeActivity, LinearLayoutManager.VERTICAL, false)
                            rv_getMenuByTime.setHasFixedSize(true)
                            rv_getMenuByTime.adapter = SearchedMenuAdapter(searchedMenuModelList)
                            Toast.makeText(this@GetMenuByTimeActivity, "${tv_now.text} ${tv_currentTime.text}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }
}