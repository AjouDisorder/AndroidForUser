package com.example.ttruserver2


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.ttruserver.ViewPagerAdapter
import com.example.ttruserver2.BottomTab.UserInfo.BottomMyInfoActivity
import com.example.ttruserver2.BottomTab.Coupon.BottomCouponActivity
import com.example.ttruserver2.BottomTab.FavoriteRestaurant.BottomFavoriteRestaurantActivity
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.OriginMenuModel
import com.example.ttruserver2.models.SearchedMenuModel
import com.example.ttruserver2.models.SearchedRestaurantModel
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.bottom.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(){
    lateinit var toolbar: Toolbar //toolbar is androidx.appcompat.widget
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    internal lateinit var viewpager : ViewPager
    lateinit var iMyService: IMyService
    private var backBtnTime: Long = 0

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitude : Double? = null
    var longitude : Double? = null
    var currentLocation : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //retrofit
        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        Locationtxt.setOnClickListener {
            val intent = Intent(this@MainActivity, SetAddressActivity::class.java)
            startActivity(intent)
        }
        if (UserData.getLng() == null){ //위치설정을 안했으니까 현재 위치로 넣자 (임시로 아주대학교 위도 경도로)
            UserData.setLng(127.046532)
            UserData.setLat(37.283602)
        }
        /*if (UserData.getLng() == null){ //위치설정을 안했으니까 현재 위치로 넣자 (임시로 아주대학교 위도 경도로)
            getLastLocation()
        }else{
            Locationtxt.text = UserData.getAddress()
        }*/

        val menuIcons = arrayOf( R.drawable.menu_time, R.drawable.menu_chickenpizza, R.drawable.menu_jokbal,
            R.drawable.menu_japan, R.drawable.menu_nation, R.drawable.menu_hambur, R.drawable.menu_rice,
            R.drawable.menu_cafe, R.drawable.menu_meat, R.drawable.menu_noodle, R.drawable.menu_snack,
            R.drawable.menu_soup, R.drawable.menu_fruit, R.drawable.menu_ricecake, R.drawable.menu_salad, R.drawable.menu_convstore)
        val menuTypes = arrayOf("시간 검색", "치킨&피자", "족발&보쌈", "돈까스&일식", "세계음식", "햄버거", "밥류",
            "카페&빵&디저트", "육고기", "면", "분식&야식", "찜&탕&찌개", "반찬&과일", "떡&기타", "샐러드&다이어트", "편의점")
        val storeIcons = arrayOf( R.drawable.store_map, R.drawable.store_buffet, R.drawable.store_drink,
            R.drawable.store_convstore, R.drawable.store_korean, R.drawable.store_chicken, R.drawable.store_pizza,
            R.drawable.store_jokbal, R.drawable.store_japan, R.drawable.store_american, R.drawable.store_fastfood,
            R.drawable.store_snack, R.drawable.store_dessert, R.drawable.store_soup, R.drawable.store_dosirak, R.drawable.store_china)
        val storeTypes = arrayOf("지도 검색", "뷔페&샐러드", "술집", "편의점", "한식", "치킨", "피자", "족발&보쌈",
            "돈까스&일식&회", "양식&아시안", "패스트푸드", "분식", "카페&디저트", "찜&탕&찌개", "도시락", "중국집")

        var selectedIconType = 0    //0일때 "할인음식", 1일때 "가게검색"
        val gridviewAdapter = GridViewAdapter(this, menuIcons, menuTypes)//conveying img and text to gridviewadapter
        main_gridview.adapter = gridviewAdapter
        discount_button.setOnClickListener {
            val gridviewAdapter = GridViewAdapter(this, menuIcons, menuTypes)//conveying img and text to gridviewadapter
            selectedIconType = 0
            tv_searchBar.hint = "메뉴 검색"
            main_gridview.adapter = gridviewAdapter
        }
        restaurant_button.setOnClickListener {
            val gridviewAdapter = GridViewAdapter(this, storeIcons, storeTypes)//conveying img and text to gridviewadapter
            selectedIconType = 1
            tv_searchBar.hint = "가게 검색"
            main_gridview.adapter = gridviewAdapter
        }

        //하단 탭
        bottom_tab_home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        bottom_tab_favorite_restaurant.setOnClickListener {
            if (UserData.getOid() == null){
                val loginIntent = Intent(this, LogInActivity::class.java)
                startActivity(loginIntent)
            }else{
                val intent = Intent(this, BottomFavoriteRestaurantActivity::class.java)
                startActivity(intent)
            }
        }
        bottom_tab_coupon.setOnClickListener {
            if (UserData.getOid() == null){
                val loginIntent = Intent(this, LogInActivity::class.java)
                startActivity(loginIntent)
            }else{
                val intent = Intent(this, BottomCouponActivity::class.java)
                startActivity(intent)
            }
        }
        bottom_tab_my_info.setOnClickListener {
            if(UserData.getOid() == null){
                val loginIntent = Intent(this, LogInActivity::class.java)
                startActivity(loginIntent)
            } else {
                val intent = Intent(this, BottomMyInfoActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        //findByCategory
        main_gridview.setOnItemClickListener { parent, view, position, id ->
            if (selectedIconType == 0){
                if (menuTypes[position] == "시간 검색"){
                    //if 시간설정인 경우 따로
                    val intent = Intent(this@MainActivity, GetMenuByTimeActivity::class.java)
                    startActivity(intent)
                }else{
                    iMyService.getMenuByCategory(menuTypes[position], UserData.getLat(), UserData.getLng()).enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            val result = response.body()?.string()
                            val jsonArray = JSONArray(result)
                            val searchedMenuModelList = arrayListOf<SearchedMenuModel>()

                            for (i in 0.until(jsonArray.length())){
                                val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                                val token = jsonObject.getString("token")
                                val _id = jsonObject.getString("_id")
                                val restaurantTitle = jsonObject.getString("restaurantTitle")
                                val restaurantOid = jsonObject.getJSONObject("originMenu").getString("restaurant_id")
                                val title = jsonObject.getString("title")
                                val startTime = jsonObject.getString("startDateObject").substring(11, 16)
                                val endTime = jsonObject.getString("endDateObject").substring(11, 16)
                                val distance = Math.round(jsonObject.getDouble("distance")/100.0)/10.0
                                val quantity = jsonObject.getInt("quantity")
                                val discount = jsonObject.getInt("discount")
                                val originPrice = jsonObject.getJSONObject("originMenu").getInt("originPrice")
                                val discountedPrice = originPrice - (originPrice * discount / 100)
                                val method = jsonObject.getString("method")

                                searchedMenuModelList.add(SearchedMenuModel(token, _id, restaurantTitle, restaurantOid, menuTypes[position], title,
                                    startTime, endTime, distance, quantity, discount, discountedPrice, originPrice, method))
                            }
                            val intent = Intent(this@MainActivity, SearchedMenuListActivity::class.java)
                            intent.putExtra("searchedMenuModelList", searchedMenuModelList)
                            startActivity(intent)
                        }
                    })
                }
            }else if(selectedIconType == 1){
                //if 지도로 보기인 경우 따로
                iMyService.getRestaurantByCategory(storeTypes[position], UserData.getLat(), UserData.getLng()).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Fail : $t", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val result = response.body()?.string()
                        val jsonArray = JSONArray(result)
                        val searchedRestaurantModelList = arrayListOf<SearchedRestaurantModel>()

                        for (i in 0.until(jsonArray.length())){
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                            val _id = jsonObject.getString("_id")
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

                            searchedRestaurantModelList.add(SearchedRestaurantModel(_id, storeTypes[position], title, grade,
                                distance, onSale, favoriteCount, description, address, phone, originMenuList,
                                lng as Double, lat as Double))
                        }
                        val intent = Intent(this@MainActivity, SearchedRestaurantListActivity::class.java)
                        intent.putExtra("searchedRestaurantModelList", searchedRestaurantModelList)
                        startActivity(intent)
                    }
                })
            }
        }
        //findBySearchBar
        tv_searchBar.setOnClickListener{
            val intent = Intent(this@MainActivity, SearchBarActivity::class.java)
            intent.putExtra("selectedIconType", selectedIconType)
            startActivity(intent)
        }

        //Advertisement
        viewpager = findViewById(R.id.main_ad_viewpager) as ViewPager
        val adapter = ViewPagerAdapter(this)
        viewpager.adapter = adapter
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    val mGeocoder = Geocoder(applicationContext, Locale.KOREAN)
                    var mResultList : List<Address>? = null

                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude
                        longitude = location.longitude
                        mResultList = mGeocoder.getFromLocation(
                            latitude!!, longitude!!, 1
                        )
                        currentLocation = mResultList[0].getAddressLine(0)
                        currentLocation = currentLocation.substring(5)
                        UserData.setLng(longitude as Double)
                        UserData.setLat(latitude as Double)
                        UserData.setAddress(currentLocation as String)
                        Locationtxt.text = currentLocation
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            UserData.setLng(mLastLocation.longitude)
            UserData.setLat(mLastLocation.latitude)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}
