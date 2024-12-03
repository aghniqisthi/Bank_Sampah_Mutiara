package com.example.banksampah.adapter

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.banksampah.DB_USERS
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewModelPengambilan(app: Application) : AndroidViewModel(app) {

    var idPengambilan: MutableLiveData<List<DocumentSnapshot>> = MutableLiveData()
    var allPengambilan: MutableLiveData<List<DocumentSnapshot>> = MutableLiveData()
    private var mDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getIdPengambilanObservers(): MutableLiveData<List<DocumentSnapshot>> {
        return idPengambilan
    }

    fun getAllPengambilanObservers(): MutableLiveData<List<DocumentSnapshot>> {
        getAllPengambilan()
        return allPengambilan
    }

    fun getIdPengambilan(uid: String) {
        mDb.collection("pengambilan-hasil")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    Log.d("cek id pengambilan", it.documents.size.toString())
                    idPengambilan.value = it.documents
                } else {
                    Log.d("empty idPengambilan", "${it}")
                }
            }
            .addOnFailureListener {
                Log.e("error idPengambilan", "${it.message} ${it.stackTrace}")
            }
    }

    fun getAllPengambilan() {
        GlobalScope.launch {
            mDb.collection("pengambilan-hasil")
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        Log.d("cek all pengambilan", it.documents.size.toString())
                        allPengambilan.postValue(it.documents)
                    } else {
                        Log.d("empty allPengambilan", "${it}")
                    }
                }
                .addOnFailureListener {
                    Log.e("error allPengambilan", "${it.message} ${it.stackTrace}")
                }
        }
    }
}