package com.example.ttruserver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.ttruserver2.Fragment.FragmentAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_grid_info.*
import kotlinx.android.synthetic.main.custom_grid_info_tab.view.*

class GridInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_info)

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        grid_info_viewpager.adapter = fragmentAdapter

        grid_info_tab_layout.addTab(grid_info_tab_layout.newTab().setCustomView(createTabView("AI")))
        grid_info_tab_layout.addTab(grid_info_tab_layout.newTab().setCustomView(createTabView("CSS")))
        grid_info_tab_layout.addTab(grid_info_tab_layout.newTab().setCustomView(createTabView("HTML ")))
        grid_info_tab_layout.addTab(grid_info_tab_layout.newTab().setCustomView(createTabView("ID")))
        grid_info_tab_layout.addTab(grid_info_tab_layout.newTab().setCustomView(createTabView("JPG")))
        grid_info_tab_layout.addTab(grid_info_tab_layout.newTab().setCustomView(createTabView("JS")))

        grid_info_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(grid_info_tab_layout))//when move page change tab

        grid_info_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{//when click tab move page
        override fun onTabReselected(p0: TabLayout.Tab?) {

        }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0 != null){
                    grid_info_viewpager.currentItem = p0.position
                }
            }

        })
    }

    private fun createTabView(tabName: String) : View {
        val gridInfoTabView = LayoutInflater.from(baseContext).inflate(R.layout.custom_grid_info_tab, null)
        gridInfoTabView.custom_grid_info_tab_txt_name.text = tabName

        return gridInfoTabView
    }



}
