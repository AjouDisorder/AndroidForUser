package com.example.ttruserver2.BottomTab.FavoriteRestaurant

import java.io.Serializable

data class BottomFavoriteRestaurantContentListModel(
    var image : Int,
    var title : String,
    var restaurnat_address : String,
    var category: String,
    var avrGrade: String,
    val saleOn: String
) : Serializable