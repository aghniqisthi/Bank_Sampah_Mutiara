package com.example.banksampah

import com.google.firebase.Timestamp
import java.text.DecimalFormat
import java.util.Date

const val SHAREDPREF_USERS : String = "DATAUSERS"
const val SHAREDPREF_UID : String = "UID"
const val SHAREDPREF_UIDDOCS : String = "UIDDOCS"
const val SHAREDPREF_FULLNAME : String = "FULLNAME"
const val SHAREDPREF_TELP : String = "TELP"
const val SHAREDPREF_ADDRESS : String = "ADDRESS"
const val SHAREDPREF_EMAIL : String = "EMAIL"
const val SHAREDPREF_ROLES : String = "ROLES"

const val DB_USERS : String = "users"

fun formattedTotal(total: Int) : String {
    return DecimalFormat("#,###").format(total)
}

fun convertDate(timestamp: Timestamp): Date {
    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    return Date(milliseconds)
}
