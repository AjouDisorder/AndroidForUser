package com.example.ttruserver2.detailRestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.OriginMenuAdapter
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.SearchedMenuAdapter
import com.example.ttruserver2.models.OriginMenuModel
import com.example.ttruserver2.models.SearchedMenuModel
import kotlinx.android.synthetic.main.fragment_menu_detail_restaurant.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MenuFragment : Fragment() {

    lateinit var iMyService: IMyService
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_menu_detail_restaurant, container, false)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        val restaurantBundle = this.arguments
        val restaurantOid = restaurantBundle?.getString("restaurantOid")
        val restaurantDistance = restaurantBundle?.getDouble("restaurantDistance")
        val originMenuList = restaurantBundle?.getParcelableArrayList<OriginMenuModel>("originMenuList")

        var searchedMenuModelList = arrayListOf<SearchedMenuModel>()
        iMyService.getMenuListOfRestaurant(restaurantOid as String).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Fail : $t", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()?.string()
                val jsonArray = JSONArray(result)

                for (i in 0.until(jsonArray.length())){
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                    val _id = jsonObject.getString("_id")
                    val restaurantTitle = jsonObject.getString("restaurantTitle")
                    val type = jsonObject.getString("type")
                    val title = jsonObject.getString("title")
                    val startTime = jsonObject.getString("startDateObject")
                        .substring(5, 16).replace("T", " ")
                    val endTime = jsonObject.getString("endDateObject")
                        .substring(5, 16).replace("T", " ")
                    val quantity = jsonObject.getInt("quantity")
                    val discount = jsonObject.getInt("discount")
                    val originPrice = jsonObject.getJSONObject("originMenu").getInt("originPrice")
                    val discountedPrice = originPrice * discount / 100
                    val method = jsonObject.getString("method")

                    searchedMenuModelList.add(SearchedMenuModel(_id, restaurantTitle, type, title, startTime, endTime,
                        restaurantDistance as Double, quantity, discount, discountedPrice, originPrice, method))

                    view.rv_menuListOnDetail.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    view.rv_menuListOnDetail.setHasFixedSize(true)
                    view.rv_menuListOnDetail.adapter = SearchedMenuAdapter(searchedMenuModelList)
                }
            }
        })
        view.rv_originMenuListOnDetail.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        view.rv_originMenuListOnDetail.setHasFixedSize(true)
        view.rv_originMenuListOnDetail.adapter = OriginMenuAdapter(originMenuList as ArrayList<OriginMenuModel>)
        return view
    }
}