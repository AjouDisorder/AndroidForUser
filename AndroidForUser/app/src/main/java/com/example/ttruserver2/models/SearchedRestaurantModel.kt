package com.example.ttruserver2.models

import java.io.Serializable

class SearchedRestaurantModel(
    val restaurantOid: String,
    val restaurantType: String,
    val restaurantTitle: String,
    val restaurantGrade: Double,
    val restaurantDistance: Double,
    val restaurantOnSale: Boolean
) : Serializable

