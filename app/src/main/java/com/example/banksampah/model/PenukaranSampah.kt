package com.example.banksampah.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.type.DateTime
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class PenukaranSampah(
    val uId:String,
    val idD:String?,
    val total_harga:Int,
    val berat:Int,
    val harga_sampah:Int,
    val jenis_sampah:String,
    val status:String,
    val tanggal_penukaran: Date,
) : Parcelable

