package com.example.ttruserver2.detailRestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.ReviewModel
import kotlinx.android.synthetic.main.fragment_review_detail_restaurant.*
import kotlinx.android.synthetic.main.fragment_review_detail_restaurant.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class ReviewFragment : Fragment() {

    lateinit var iMyService: IMyService
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_review_detail_restaurant, container, false)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        val restaurantBundle = this.arguments
        val restaurantOid = restaurantBundle?.getString("restaurantOid")
        val reviewList = arrayListOf<ReviewModel>()

        iMyService.getReviewList(restaurantOid).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Fail : $t", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()?.string()
                val jsonArray = JSONArray(result)

                if (jsonArray.length() == 0){
                    tv_nonReview.visibility = View.VISIBLE
                }else{
                    for (i in 0.until(jsonArray.length())){
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                        val _id = jsonObject.getString("_id")
                        val ticket_id = jsonObject.getJSONObject("ticket").getString("_id")
                        val userName = jsonObject.getJSONObject("ticket").getString("userName")
                        val menuName = jsonObject.getJSONObject("ticket").getString("menuName")
                        val grade = jsonObject.getDouble("grade")
                        val description = jsonObject.getString("description")

                        reviewList.add(0, ReviewModel(_id, ticket_id, userName, menuName, grade, description))

                    }
                    view.rv_reviewList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    view.rv_reviewList.setHasFixedSize(true)
                    view.rv_reviewList.adapter = ReviewAdapter(reviewList)
                }
            }
        })
        return view
    }
}