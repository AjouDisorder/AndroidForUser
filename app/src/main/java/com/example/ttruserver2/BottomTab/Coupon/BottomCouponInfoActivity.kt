package com.example.ttruserver2.BottomTab.Coupon

import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.SearchedRestaurantDetailActivity
import com.example.ttruserver2.UserData
import com.example.ttruserver2.models.TicketModel
import com.example.ttruserver2.models.OriginMenuModel
import com.example.ttruserver2.models.SearchedRestaurantModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_bottom_coupon_info.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomCouponInfoActivity : AppCompatActivity() {
    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_coupon_info)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        val selectedTicket = intent.getSerializableExtra("selectedTicket") as TicketModel

        bottom_coupon_info_lecture_text.setText(selectedTicket.menuName)
        bottom_coupon_info_price_real_text.setText(selectedTicket.restaurantTitle)
        bottom_coupon_info_price_real_text4.setText(selectedTicket.address)
        var method_kr_ : String? = null
        if(selectedTicket.method == "forhere") {
            method_kr_ = "매장 식사"
        }else if(selectedTicket.method == "takeout"){
            method_kr_ = "포장"
        }
        bottom_coupon_info_price_real_text3.setText(method_kr_)
        bottom_coupon_info_price_real_text8.setText(selectedTicket.quantity.toString())
        bottom_coupon_info_price_real_text10.setText(selectedTicket.totalPrice.toString())

        tv_availableTime.text = "${selectedTicket.startDateString.substring(11, 16)} ~ " +
                "${selectedTicket.endDateString.substring(11, 16)} " +
                "(${selectedTicket.endDateString.substring(0, 10)} 까지)"

        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix: BitMatrix = multiFormatWriter.encode(selectedTicket.value, BarcodeFormat.QR_CODE, 600, 600)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap : Bitmap = barcodeEncoder.createBitmap(bitMatrix)

        bottom_coupon_info_qrcode.setImageBitmap(bitmap)

        tv_mapInTicket.setOnClickListener {
            val location = Uri.parse("kakaomap://route?sp="+UserData.getLat()+","+UserData.getLng()+"&ep="+selectedTicket.location_lat+","+selectedTicket.location_lng+"&by=FOOT")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)

            val activities: List<ResolveInfo> = packageManager.queryIntentActivities(mapIntent, 0)
            val isIntentSafe: Boolean = activities.isNotEmpty()

            if (isIntentSafe) {
                startActivity(mapIntent)
            }
        }
        tv_gotoRestaurant.setOnClickListener {
            iMyService.getRestaurantDetail(selectedTicket.restaurant_id).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@BottomCouponInfoActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val result = response?.body()?.string()
                    val jsonObject = JSONObject(result)

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

                    val selectedRestaurant = SearchedRestaurantModel(_id, type, title, grade,
                        distance, onSale, favoriteCount, description, address, phone, originMenuList,
                        lng as Double, lat as Double)

                    val intent = Intent(this@BottomCouponInfoActivity, SearchedRestaurantDetailActivity::class.java)
                    intent.putExtra("selectedRestaurant", selectedRestaurant)
                    this@BottomCouponInfoActivity.startActivity(intent)
                }
            })
        }

        //review btn disable
        if (selectedTicket.available == false){ //사장님 인증 이후
            btn_gotoReview.isEnabled = true
            iMyService.getReviewedTicketList(UserData.getOid()).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@BottomCouponInfoActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val result = response.body()?.string()
                    val jsonArray = JSONArray(result)
                    for (i in 0.until(jsonArray.length())){
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        if (selectedTicket.ticket_id == jsonObject.getString("_id")){
                            btn_gotoReview.isEnabled = false
                            break
                        }
                    }
                }
            })
        }else{
            btn_gotoReview.isEnabled = false
        }

        //create review
        btn_gotoReview.setOnClickListener {
            val intent = Intent(this, CreateReviewActivity::class.java)
            intent.putExtra("selectedTicket", selectedTicket)
            startActivity(intent)
            finish()
        }
    }
}
