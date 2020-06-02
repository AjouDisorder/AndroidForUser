package com.example.ttruserver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ttruserver2.models.SearchedMenuModel
import kotlinx.android.synthetic.main.searched_menu_detail.*

class SearchedMenuDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searched_menu_detail)

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
    }
}
