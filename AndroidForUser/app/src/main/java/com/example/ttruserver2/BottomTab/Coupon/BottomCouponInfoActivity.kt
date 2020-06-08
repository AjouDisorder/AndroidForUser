package com.example.ttruserver2.BottomTab.Coupon

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ttruserver2.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_bottom_coupon_info.*

class BottomCouponInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_coupon_info)

        val location_lat = intent.getStringExtra("location_lat")
        val location_lng = intent.getStringExtra("location_lng")
        val available = intent.getStringExtra("available")
        val coupon_id = intent.getStringExtra("coupon_id")
        val restaurant_id = intent.getStringExtra("restaurant_id")
        val address = intent.getStringExtra("address")
        val quantity = intent.getStringExtra("quantity")
        val totalPrice = intent.getStringExtra("totalPrice")
        val userName = intent.getStringExtra("userName")
        val menuName = intent.getStringExtra("menuName")
        val method = intent.getStringExtra("method")
        val value = intent.getStringExtra("value")
        val messageForBoss = intent.getStringExtra("messageForBoss")
        var restaurantTitle = intent.getStringExtra("restaurantTitle")

//        val couponInfo = intent.getStringExtra("coupon_info")
//        Toast.makeText(this, "넘어온 정보: " + couponInfo, Toast.LENGTH_LONG).show()
//        val asd = couponInfo.split(',')
//        val qwe = asd[3].split("=",")")
//        Log.d("asd", qwe[1].toString())
//        bottom_coupon_info_lecture_text.setText()
        bottom_coupon_info_lecture_text.setText(menuName)
        bottom_coupon_info_price_real_text.setText(restaurantTitle)
        bottom_coupon_info_price_real_text4.setText(address)
        var method_kr_ : String? = null
        if(method == "forhere") {
            method_kr_ = "매장 식사"
        }else if(method == "takeout"){
            method_kr_ = "포장"
        }
        bottom_coupon_info_price_real_text3.setText(method_kr_)
        bottom_coupon_info_price_real_text8.setText(quantity.toString())
        bottom_coupon_info_price_real_text10.setText(totalPrice)
//        coupondata_test5.setText(value)

        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix: BitMatrix = multiFormatWriter.encode(value, BarcodeFormat.QR_CODE, 600, 600)
//        val bitMatrix: BitMatrix = multiFormatWriter.encode("UserInputText", BarcodeFormat.QR_CODE, 600, 600)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap : Bitmap = barcodeEncoder.createBitmap(bitMatrix)

        bottom_coupon_info_qrcode.setImageBitmap(bitmap)
    }
}
