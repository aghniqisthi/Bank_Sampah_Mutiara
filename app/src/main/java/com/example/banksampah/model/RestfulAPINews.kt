package com.example.banksampah.model

import retrofit2.Call
import retrofit2.http.GET

interface RestfulAPINews {
    @GET("everything?q=sampah&sortBy=publishedAt&apiKey=71ea99d001f5427682a68af2429c83ae")
    fun getAllNews() : Call<ResponseDataBerita>
}