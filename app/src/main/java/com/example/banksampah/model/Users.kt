package com.example.banksampah.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val uId:String,
    val fullname:String,
    val telp : String,
    val address:String,
    val email:String,
    val password:String?,
    val roles:String,
) : Parcelable
