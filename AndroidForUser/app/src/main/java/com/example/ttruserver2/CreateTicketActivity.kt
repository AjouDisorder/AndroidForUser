package com.example.ttruserver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.SearchedMenuModel
import kotlinx.android.synthetic.main.activity_create_ticket.*
import kr.co.bootpay.Bootpay
import kr.co.bootpay.BootpayAnalytics
import kr.co.bootpay.enums.UX
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTicketActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService
    private val application_id = "5eddbc748f0751002bfcd4eb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ticket)

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(this, application_id)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)
        val selectedMenu = intent.getSerializableExtra("selectedMenu") as SearchedMenuModel

        //menu data assign
        tv_menuTitleInPay.text = selectedMenu.menuTitle
        tv_totalPriceInPay.text = selectedMenu.discountedPrice.toString()
        val menuMethod = selectedMenu.method
        val availableQuantity = selectedMenu.quantity
        tv_availableQuantity.text = availableQuantity.toString()
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

                    //START BOOTPAY
                    val stuck:Int = selectedMenu.quantity-quantity
                    val price:Int = selectedMenu.discountedPrice * quantity
                    val title:String = selectedMenu.menuTitle
                    val mOid:String = selectedMenu.menuOid

                    val bootUser = BootUser().setPhone(UserData.getPhone())
                    val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3))

                    Bootpay.init(fragmentManager)
                        .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                        //.setPG(PG.KAKAO) // 결제할 PG 사
                        //.setMethod(Method.EASY) // 결제수단
                        .setContext(this)
                        .setBootUser(bootUser)
                        .setBootExtra(bootExtra)
                        .setUX(UX.PG_DIALOG) //                .setUserPhone("010-1234-5678") // 구매자 전화번호
                        .setName(title) // 결제할 상품명
                        .setOrderId(mOid) // 결제 고유번호 menuOid
                        .setPrice(price) // 결제할 금액
                        .addItem(title, quantity, mOid, price) // 주문정보에 담길 상품정보, 통계를 위해 사용
                        .onConfirm { message ->
                            // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                            if (0 < stuck) Bootpay.confirm(message) // 재고가 있을 경우.
                            else Bootpay.removePaymentWindow() // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                            Log.d("confirm", message)
                        }
                        .onDone { message ->
                            // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                            Log.d("done", message)
                            iMyService.createTicket(selectedMenu.menuOid, quantity, UserData.getOid(), tv_totalPriceInPay.text as String,
                                ticketMethod, tv_messageForBoss.text, selectedMenu.restaurantTitle).enqueue(object : Callback<ResponseBody>{
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Toast.makeText(this@CreateTicketActivity, "$t", Toast.LENGTH_LONG).show()
                                }
                                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                                    val result = response?.body()?.string()
                                    val jsonObject = JSONObject(result)
                                    //서버 수량 초과
                                    if(jsonObject.has("result")){
                                        Toast.makeText(this@CreateTicketActivity, "메뉴 수량이 부족합니다", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(this@CreateTicketActivity, "구매가 완료되었습니다", Toast.LENGTH_SHORT).show()
                                        iMyService.messageForBoss(selectedMenu.token, "메세지 : ${tv_messageForBoss.text}  \n" +
                                                "${selectedMenu.restaurantTitle}의 ${selectedMenu.menuTitle} 메뉴").enqueue(object : Callback<ResponseBody>{
                                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                                Toast.makeText(this@CreateTicketActivity, "$t", Toast.LENGTH_LONG).show()
                                            }
                                            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                                                val intent = Intent(this@CreateTicketActivity, MainActivity::class.java)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                startActivity(intent)
                                            }
                                        })
                                    }
                                }
                            })
                        }
                        .onReady { message ->
                            // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                            Log.d("ready", message)
                        }
                        .onCancel { message ->
                            // 결제 취소시 호출
                            Log.d("cancel", message)
                        }
                        .onError { message ->
                            // 에러가 났을때 호출되는 부분
                            Log.d("error", message)
                        }
                        .onClose { Log.d("close", "close") }
                        .request()
                }
            }
        }
    }
}