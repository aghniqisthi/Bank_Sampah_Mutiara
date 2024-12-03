package com.example.banksampah.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientNews {
    private const val BASE_URL = "https://newsapi.org/v2/"

    val instance : RestfulAPINews by lazy {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        retrofit.create(RestfulAPINews::class.java)
    }
}