package com.example.ttruserver2.models

import java.io.Serializable

class ReviewModel (
    val reviewOid: String,
    val ticket_id : String,
    val userName : String,
    val menuName : String,
    val grade : Double,
    val description : String
) : Serializable