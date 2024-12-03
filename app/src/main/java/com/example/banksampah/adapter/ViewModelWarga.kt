package com.example.banksampah.adapter

import android.app.Application
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.banksampah.DB_USERS
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

class ViewModelWarga(app: Application) : AndroidViewModel(app) {

    private var mDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var allWarga: MutableLiveData<List<DocumentSnapshot>> = MutableLiveData()
    private var searchWarga: MutableLiveData<List<DocumentSnapshot>> = MutableLiveData()
    private var totalPenukaran: MutableLiveData<List<Int>> = MutableLiveData()
    private var totalKas: MutableLiveData<Int> = MutableLiveData()

    private var isError: MutableLiveData<String> = MutableLiveData()
//    private var totalSampahDiterima : MutableLiveData<Int> = MutableLiveData()
//    private var totalSampahBerhasil : MutableLiveData<Int> = MutableLiveData()

    init {
        getAllWarga()
    }

    fun getAllWargaObservers(): MutableLiveData<List<DocumentSnapshot>> {
        return allWarga
    }

    fun getSearchWargaObservers(): MutableLiveData<List<DocumentSnapshot>> {
        return searchWarga
    }

    fun getError(): MutableLiveData<String> {
        return isError
    }

    fun getTotalPenukaranObservers(year: String): MutableLiveData<List<Int>> {
        getTotalPenukaranTahunan(year.toInt())
        return totalPenukaran
    }

    fun getTotalKasObservers(year: String): MutableLiveData<Int> {
        getTotalKasTahunan(year.toInt())
        return totalKas
    }

    private fun getAllWarga() {
        GlobalScope.launch {
            mDb.collection(DB_USERS).get()
                .addOnSuccessListener {
                    if (it != null) {
                        Log.d(
                            "cek 12 fullname",
                            "${
                                it.documents[0].data?.get("fullname").toString()
                            } ; ${it.documents[1].data?.get("fullname").toString()}"
                        )

                        allWarga.postValue(it.documents)
                    } else {
                        Log.d("empty getallwarga", "${it}")
                        isError.postValue("Data Kosong")
                    }
                }
                .addOnFailureListener {
                    Log.e("error getallwarga", "${it.message} ${it.stackTrace}")
                    isError.postValue("Error ${it.cause} : ${it.message}")
                }
        }
    }

    fun searchWarga(username:String) {
        GlobalScope.launch {
            mDb.collection(DB_USERS).get()
                .addOnSuccessListener {
                    if (it != null) {
                        val filteredUsers = it.filter {
                            it.getString("fullname")?.toLowerCase()?.contains(username.toLowerCase()) == true
                        }
                        searchWarga.postValue(filteredUsers)
                    } else {
                        Log.d("empty search", "${it}")
                        isError.postValue("Data Kosong")
                    }
                }
                .addOnFailureListener {
                    Log.e("error search", "${it.message} ${it.stackTrace}")
                    isError.postValue("Error ${it.cause} : ${it.message}")
                }
        }
    }

    private fun getTotalPenukaranTahunan(tahun: Int) {
        val newYear = Timestamp(Date(tahun - 1900, 0, 1))
        val endYear = Timestamp(Date(tahun - 1900, 11, 31))

        GlobalScope.launch {
            mDb.collection("penukaran-sampah")
                .orderBy("tanggal_penukaran")
                .startAt(newYear)
                .endAt(endYear)
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        Log.d("cek totalpenukaran", "${it.documents.size}")

                        var diterima = 0
                        var berhasil = 0

                        it.documents.forEach {
                            var data = it.data?.get("status")
                            if (data == "Sampah telah diterima") {
                                diterima = diterima + 1
                            } else if (data == "Hasil penukaran dapat diambil di Bank Sampah") {
                                berhasil = berhasil + 1
                            }
                        }

                        totalPenukaran.postValue(listOf(it.documents.size, diterima, berhasil))

//                        totalSampahDiterima.postValue(diterima)
//                        totalSampahBerhasil.postValue(berhasil)
                    } else {
                        Log.d("empty totalpenukaran", "${it}")
                    }
                }
                .addOnFailureListener {
                    Log.e("error totalpenukaran", "${it.message} ${it.stackTrace}")
                }
        }
    }


    private fun getTotalKasTahunan(tahun: Int) {
        val newYear = Timestamp(Date(tahun - 1900, 0, 1))
        val endYear = Timestamp(Date(tahun - 1900, 11, 31))

        GlobalScope.launch {
            mDb.collection("pengambilan-hasil")
                .orderBy("tanggal_pengambilan")
                .startAt(newYear)
                .endAt(endYear)
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        Log.d("cek totalpengambilan", "${it.documents.size}")

                        var _totalKas = 0
                        it.documents.forEach {
                            _totalKas = _totalKas + (it.data?.get("masuk_kas")
                                .toString().toIntOrNull() ?: 0)
                        }

                        totalKas.postValue(_totalKas)

                    } else {
                        Log.d("empty totalpenukaran", "${it}")
                    }
                }
                .addOnFailureListener {
                    Log.e("error totalpenukaran", "${it.message} ${it.stackTrace}")
                }
        }
    }

    private fun getRiwayatWarga(uid: String) {
        GlobalScope.launch {
            mDb.collection("penukaran-sampah")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        Log.d(
                            "cek riwayat",
                            "${it.documents.first().data?.get("fullname").toString()}"
                        )
                        allWarga.postValue(it.documents)
                    } else {
                        Log.d("empty getriwayat", "${it}")
                    }
                }
                .addOnFailureListener {
                    Log.e("error getriwayat", "${it.message} ${it.stackTrace}")
                }
        }
    }
}