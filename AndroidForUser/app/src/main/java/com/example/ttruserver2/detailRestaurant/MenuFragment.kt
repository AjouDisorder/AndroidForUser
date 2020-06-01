package com.example.ttruserver2.detailRestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttruserver2.OriginMenuAdapter
import com.example.ttruserver2.R
import com.example.ttruserver2.SearchedMenuAdapter
import com.example.ttruserver2.models.OriginMenuModel
import com.example.ttruserver2.models.SearchedMenuModel
import kotlinx.android.synthetic.main.fragment_menu_detail_restaurant.view.*

class MenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_menu_detail_restaurant, container, false)

        val menuList = arrayListOf(
            SearchedMenuModel(
                "tmp", "카페&빵&디저트", "어떤 커피", "16:00",
                "18:00", 0.6, 5, 50, 7500, 15000
            ),
            SearchedMenuModel(
                "tmp", "카페&빵&디저트", "어떤 커피", "16:00",
                "18:00", 0.6, 5, 50, 7500, 15000
            ),
            SearchedMenuModel(
                "tmp", "족발&보쌈", "맛있는 족발", "15:00",
                "17:00", 1.2, 30, 70, 6000, 20000
            )
        )

        val originMenuList = arrayListOf(
            OriginMenuModel("후라이드 치킨", 11000),
            OriginMenuModel("간장 치킨", 12000)
        )

        view.rv_menuListOnDetail.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        view.rv_menuListOnDetail.setHasFixedSize(true)
        view.rv_menuListOnDetail.adapter = SearchedMenuAdapter(menuList)
        view.rv_originMenuListOnDetail.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        view.rv_originMenuListOnDetail.setHasFixedSize(true)
        view.rv_originMenuListOnDetail.adapter = OriginMenuAdapter(originMenuList)
        return view
    }
}