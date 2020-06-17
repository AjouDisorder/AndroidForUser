package com.example.ttruserver2

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.OriginMenuModel
import com.example.ttruserver2.models.SearchedMenuModel
import com.example.ttruserver2.models.SearchedRestaurantModel
import kotlinx.android.synthetic.main.searched_menu_detail.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchedMenuDetailActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searched_menu_detail)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        val selectedMenu = intent.getSerializableExtra("selectedMenu") as SearchedMenuModel
        val menuTypeToIcons = hashMapOf( "치킨&피자" to R.drawable.menu_chickenpizza, "족발&보쌈" to R.drawable.menu_jokbal,
            "돈까스&일식" to R.drawable.menu_japan, "세계음식" to R.drawable.menu_nation, "햄버거" to R.drawable.menu_hambur,
            "밥류" to R.drawable.menu_rice, "카페&빵&디저트" to R.drawable.menu_cafe, "육고기" to R.drawable.menu_meat,
            "면" to R.drawable.menu_noodle, "분식&야식" to R.drawable.menu_snack, "찜&탕&찌개" to R.drawable.menu_soup,
            "반찬&과일" to R.drawable.menu_fruit, "떡&기타" to R.drawable.menu_ricecake,
            "샐러드&다이어트" to R.drawable.menu_salad, "편의점" to R.drawable.menu_convstore)

        //menu data assign
        tv_restaurantTitleInMenu.text = selectedMenu.restaurantTitle
        tv_titleInMenu.text = selectedMenu.menuTitle
        tv_originPriceInMenu.text = selectedMenu.originPrice.toString()
        tv_discountPriceInMenu.text = selectedMenu.discountedPrice.toString()
        tv_discountInMenu.text = selectedMenu.discount.toString()
        searched_menu_pic.setImageResource(menuTypeToIcons[selectedMenu.menuType]!!)

        btn_toPayment.setOnClickListener{
            var intent = Intent(this, CreateTicketActivity::class.java)
            intent.putExtra("selectedMenu", selectedMenu)
            startActivity(intent)
        }

        btn_toRestaurant.setOnClickListener {
            iMyService.getRestaurantDetail(selectedMenu.restaurantOid).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@SearchedMenuDetailActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val result = response?.body()?.string()
                    val jsonObject = JSONObject(result)

                    val _id = jsonObject.getString("_id")
                    val type = jsonObject.getString("type")
                    val title = jsonObject.getString("title")
                    val grade = jsonObject.getDouble("avrGrade")
                    val distance = selectedMenu.menuDistance
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

                    val selectedRestaurant = SearchedRestaurantModel(_id, type, title, grade,
                        distance, onSale, favoriteCount, description, address, phone, originMenuList,
                        lng as Double, lat as Double)

                    val intent = Intent(this@SearchedMenuDetailActivity, SearchedRestaurantDetailActivity::class.java)
                    intent.putExtra("selectedRestaurant", selectedRestaurant)
                    this@SearchedMenuDetailActivity.startActivity(intent)
                }
            })
        }
        button2.setOnClickListener{
            iMyService.getRestaurantDetail(selectedMenu.restaurantOid).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@SearchedMenuDetailActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val result = response?.body()?.string()
                    val jsonObject = JSONObject(result)

                    val lng = jsonObject.getJSONObject("location").getJSONArray("coordinates").get(0)
                    val lat= jsonObject.getJSONObject("location").getJSONArray("coordinates").get(1)
                    val location = Uri.parse("kakaomap://route?sp="+UserData.getLat()+","+UserData.getLng()+"&ep="+lat+","+lng+"&by=FOOT")
                    val mapIntent = Intent(Intent.ACTION_VIEW, location)

                    val activities: List<ResolveInfo> = packageManager.queryIntentActivities(mapIntent, 0)
                    val isIntentSafe: Boolean = activities.isNotEmpty()

                    if (isIntentSafe) {
                        startActivity(mapIntent)
                    }


                }
            })
        }
    }
}
