package com.example.ttruserver2.BottomTab.Coupon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.UserData
import com.example.ttruserver2.models.TicketModel
import kotlinx.android.synthetic.main.activity_bottom_coupon_info.*
import kotlinx.android.synthetic.main.activity_create_review.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateReviewActivity : AppCompatActivity() {
    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_review)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        val selectedTicket = intent.getSerializableExtra("selectedTicket") as TicketModel

        //ticket data assign
        tv_reviewRestaurantTitle.text = selectedTicket.restaurantTitle
        tv_reviewMenuTitle.text = selectedTicket.menuName

        btn_createReview.setOnClickListener {
            iMyService.createReview(selectedTicket.ticket_id, rb_setGrade.rating, reviewDescription.text, UserData.getOid())
                .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@CreateReviewActivity, "$t", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    Toast.makeText(this@CreateReviewActivity, "리뷰 등록이 완료되었습니다", Toast.LENGTH_LONG).show()
                    finish()
                }
            })
        }
    }
}