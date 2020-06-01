package com.example.ttruserver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.models.SearchedMenuModel
import kotlinx.android.synthetic.main.activity_searched_menu_list.*

class SearchedMenuListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_menu_list)

        var menuList =
            intent.getSerializableExtra("searchedMenuModelList") as ArrayList<SearchedMenuModel>

        rv_menuList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_menuList.setHasFixedSize(true)
        rv_menuList.adapter = SearchedMenuAdapter(menuList)
    }
}
