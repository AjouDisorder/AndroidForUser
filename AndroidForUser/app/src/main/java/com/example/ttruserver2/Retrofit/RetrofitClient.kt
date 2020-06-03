package com.example.ttruserver2.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var instance: Retrofit?=null

    fun getInstance(): Retrofit {
        if(instance == null)
            instance = Retrofit.Builder()
                .baseUrl("http://101.101.211.145:3000") //"http://49.50.172.13:3000"
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return instance!!
    }
}
