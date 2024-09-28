package com.example.mobiletodolist.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.43.43:4000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val api: ApiService = retrofit.create(ApiService::class.java)
}