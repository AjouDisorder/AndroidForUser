package com.example.ttruserver2.models

import java.io.Serializable

class TicketModel(
    val ticketOid: String,
    val address: String,
    val quantity: Int,
    val totalPrice: Int,
    val userName: String,
    val menuName: String,
    val method: String,
    val value: String,
    val lng: Double,
    val lat: Double
) : Serializable