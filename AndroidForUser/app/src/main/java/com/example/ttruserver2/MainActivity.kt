package com.example.ttruserver2


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.ttruserver.ViewPagerAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toolbar: Toolbar //toolbar is androidx.appcompat.widget
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    internal lateinit var viewpager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//navigation
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
//navigation

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
            et_searchBar.setHint("메뉴 검색")
            main_gridview.adapter = gridviewAdapter
        }
        restaurant_button.setOnClickListener {
            val gridviewAdapter = GridViewAdapter(this, storeIcons, storeTypes)//conveying img and text to gridviewadapter
            selectedIconType = 1
            et_searchBar.setHint("가게 검색")
            main_gridview.adapter = gridviewAdapter
        }
        //findByCategory
        main_gridview.setOnItemClickListener { parent, view, position, id ->
            if (selectedIconType == 0){
                Toast.makeText(this, "카테고리 : ${menuTypes[position]}", Toast.LENGTH_SHORT).show()
            }else if(selectedIconType == 1){
                Toast.makeText(this, "카테고리 : ${storeTypes[position]}", Toast.LENGTH_SHORT).show()
            }
        }
        //findBySearchBar
        iv_searchBtn.setOnClickListener{
            var intent = Intent(this, SearchedMenuListActivity::class.java)
            if (selectedIconType == 0){
                intent = Intent(this, SearchedMenuListActivity::class.java)
            }else if(selectedIconType == 1){
                intent = Intent(this, SearchedRestaurantListActivity::class.java)
            }
            startActivity(intent)
        }


        //Advertisement
        viewpager = findViewById(R.id.main_ad_viewpager) as ViewPager
        val adapter = ViewPagerAdapter(this)
        viewpager.adapter = adapter
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.user_profile-> {
                Toast.makeText(this, "user_profile clicked", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, LogInActivity::class.java)
//                startActivity(intent)
            }
            R.id.navigation_home-> {
                Toast.makeText(this, "navigation_home clicked!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LogInActivity::class.java)
                val intent2 = Intent(this, TestActivity::class.java)
                startActivity(intent)
            }
            R.id.navigation_bike -> {
                Toast.makeText(this, "navigation_bike clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_bus -> {
                Toast.makeText(this, "navigation_bus clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_airplane -> {
                Toast.makeText(this, "navigation_airplane clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_login -> {
                Toast.makeText(this, "navigation_login clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_profile -> {
                Toast.makeText(this, "navigation_profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_logout -> {
                Toast.makeText(this, "navigation_logout clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_cloud -> {
                Toast.makeText(this, "navigation_cloud clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_wifi -> {
                Toast.makeText(this, "navigation_wifi clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    //navigation back btn process
    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }
}
