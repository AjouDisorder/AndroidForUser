package com.example.ttruserver2.models

import java.io.Serializable

class SearchedRestaurantModel(
    val restaurantOid: String,
    val type: String,
    val title: String,
    val grade: Double,
    val distance: Double,
    val onSale: Boolean,

    val favoriteCount: Int,
    val description: String,
    val address: String,
    val phone: String,
    val originMenuList: ArrayList<OriginMenuModel>,
    val lng: Double,
    val lat: Double
) : Serializable

