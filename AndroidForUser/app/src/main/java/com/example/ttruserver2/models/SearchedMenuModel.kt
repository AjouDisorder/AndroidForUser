package com.example.ttruserver2.models

import java.io.Serializable

class SearchedMenuModel(
    val menuOid: String,
    val menuType: String,
    val menuTitle: String,
    val startTime: String,
    val endTime: String,
    val menuDistance: Double,
    val quantity: Int,
    val discount: Int,
    val discountedPrice: Int,
    val originPrice: Int
) : Serializable