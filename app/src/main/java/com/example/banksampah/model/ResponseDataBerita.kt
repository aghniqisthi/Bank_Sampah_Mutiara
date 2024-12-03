package com.example.banksampah.model

data class ResponseDataBerita(
    val articles: List<Article?>?,
    val status: String?,
    val totalResults: Int?
)