package com.example.ttruserver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.SearchedMenuModel
import kotlinx.android.synthetic.main.activity_create_ticket.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTicketActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ticket)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)
        val selectedMenu = intent.getSerializableExtra("selectedMenu") as SearchedMenuModel

        //menu data assign
        tv_menuTitleInPay.text = selectedMenu.menuTitle
        tv_totalPriceInPay.text = selectedMenu.discountedPrice.toString()
        val menuMethod = selectedMenu.method
        val availableQuantity = selectedMenu.quantity
        var quantity = 1

        //method radio box
        if (menuMethod == "forhere"){
            rb_takeout.visibility = View.INVISIBLE
        }else if(menuMethod == "takeout"){
            rb_forhere.visibility = View.INVISIBLE
        }
        //btn quantity
        if (availableQuantity == 1){
            btn_plusQuantity.isEnabled = false
        }
        btn_plusQuantity.setOnClickListener{
            tv_ticketQuantity.text = (quantity + 1).toString()
            quantity += 1
            if (quantity == availableQuantity){
                btn_plusQuantity.isEnabled = false
            }
            if(quantity > 1){
                btn_minusQuantity.isEnabled = true
            }
            tv_totalPriceInPay.text = (selectedMenu.discountedPrice * quantity).toString()
        }
        btn_minusQuantity.setOnClickListener {
            tv_ticketQuantity.text = (quantity - 1).toString()
            quantity -= 1
            if (quantity == 1){
                btn_minusQuantity.isEnabled = false
            }
            if(quantity < availableQuantity){
                btn_plusQuantity.isEnabled = true
            }
            tv_totalPriceInPay.text = (selectedMenu.discountedPrice * quantity).toString()
        }

        //create ticket
        btn_createTicket.setOnClickListener{
            if (UserData.getOid() == null){
                intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }else{
                lateinit var ticketMethod : String
                if (!rb_forhere.isChecked && !rb_takeout.isChecked){
                    Toast.makeText(this@CreateTicketActivity, "결제 방식을 선택해주세요", Toast.LENGTH_LONG).show()
                }else{
                    if (rb_forhere.isChecked){
                        ticketMethod = "forhere"
                    }else if(rb_takeout.isChecked){
                        ticketMethod = "takeout"
                    }
                    iMyService.createTicket(selectedMenu.menuOid, quantity, UserData.getOid(), tv_totalPriceInPay.text as String,
                        ticketMethod, tv_messageForBoss.text).enqueue(object : Callback<ResponseBody>{
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@CreateTicketActivity, "$t", Toast.LENGTH_LONG).show()
                        }
                        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                            val result = response?.body()?.string()
                            val jsonObject = JSONObject(result)
                            //서버 수량 초과
                            if(jsonObject.has("result")){
                                //Toast.makeText(this@CreateTicketActivity, "${UserData.getOid()}", Toast.LENGTH_LONG).show()
                                Toast.makeText(this@CreateTicketActivity, "메뉴 수량이 부족합니다", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this@CreateTicketActivity, "구매가 완료되었습니다", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@CreateTicketActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                            /*{
                                val ticketOid = jsonObject.getString("_id")
                                val address = jsonObject.getString("address")
                                val quantity = jsonObject.getInt("quantity")
                                val totalPrice = jsonObject.getInt("totalPrice")
                                val userName = jsonObject.getString("userName")
                                val menuName = jsonObject.getString("menuName")
                                val method = jsonObject.getString("method")
                                val value = jsonObject.getString("value")
                                val lng= jsonObject.getJSONObject("location").getJSONArray("coordinates").get(0)
                                val lat= jsonObject.getJSONObject("location").getJSONArray("coordinates").get(1)
                                val ticketModel = TicketModel(ticketOid, address, quantity, totalPrice, userName, menuName,
                                    method, value, lng as Double, lat as Double)

                                val intent = Intent(this@CreateTicketActivity, MainActivity::class.java)
                                intent.putExtra("ticketModel", ticketModel)
                                startActivity(intent)
                            }*/
                        }
                    })
                }
            }
        }
    }
}