package com.example.banksampah.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.type.DateTime
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class PengambilanHasil(
    val uId:String,
    val idD: String?,
    val masuk_kas:Int,
    val persentase_kas:Int,
    val saldo_diambil_akhir:Int,
    val saldo_diambil_awal:Int,
    val sisa_saldo:Int,
    val tanggal_pengambilan: Date,
) : Parcelable