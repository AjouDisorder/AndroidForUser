package com.example.ttruserver2.models

data class BottomCouponContentsListModel(
    var image : Int,
    var location_lat: Double,
    var location_lng: Double,
    var available: String,
    var coupon_id: String,
    var restaurant_id: String,
    var address: String,
    var quantity: Int,
    var totalPrice: Int,
    var userName: String,
    var menuName: String,
    var method: String,
    var value: String,
    var messageForBoss: String,
    var restaurantTitle: String
)