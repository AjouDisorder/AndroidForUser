package com.example.ttruserver2.models

import java.io.Serializable

data class TicketModel(
    val image : Int,
    val location_lat: Double,
    val location_lng: Double,
    val available: Boolean,
    val ticket_id: String,
    val restaurant_id: String,
    val address: String,
    val quantity: Int,
    val totalPrice: Int,
    val userName: String,
    val menuName: String,
    val method: String,
    val value: String,
    val messageForBoss: String,
    val restaurantTitle: String,
    val startDateString: String,
    val endDateString: String
) : Serializable