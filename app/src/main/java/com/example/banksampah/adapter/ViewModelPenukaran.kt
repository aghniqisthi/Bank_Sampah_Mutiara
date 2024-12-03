package com.example.banksampah.adapter

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewModelPenukaran(app: Application) : AndroidViewModel(app) {

    private var mDb : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var idAllPenukaran : MutableLiveData<List<DocumentSnapshot>> = MutableLiveData()
    private var allPenukaran : MutableLiveData<List<DocumentSnapshot>> = MutableLiveData()

    fun getIdAllPenukaranObservers(): MutableLiveData<List<DocumentSnapshot>> {
        return idAllPenukaran
    }
    fun getAllPenukaranObservers(): MutableLiveData<List<DocumentSnapshot>> {
        getAllPenukaranWarga()
        return allPenukaran
    }

    fun getIdAllPenukaranWarga(uid: String) {
        GlobalScope.launch {
            mDb.collection("penukaran-sampah")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener {
                    if(it != null){
                        Log.d("cek riwayat", "$uid ${it.documents.size}")
                        idAllPenukaran.postValue(it.documents)
                    } else {
                        Log.d("empty getriwayat", "${it}")
                    }
                }
                .addOnFailureListener {
                    Log.e("error getriwayat", "${it.message} ${it.stackTrace}")
                }
        }
    }

    fun getAllPenukaranWarga() {
        GlobalScope.launch {
            mDb.collection("penukaran-sampah")
                .get()
                .addOnSuccessListener {
                    if(it != null){
                        Log.d("cek all tukar", "${it.documents.size}")
                        allPenukaran.postValue(it.documents)
                    } else {
                        Log.d("empty all tukar", "${it}")
                    }
                }
                .addOnFailureListener {
                    Log.e("error all tukar", "${it.message} ${it.stackTrace}")
                }
        }
    }

//    fun insertNote(entity: Notes){
//        val noteDao = NotesDatabase.getInstance(getApplication())?.noteDao()
//        noteDao!!.insertNotes(entity)
//        getAllNote()
//    }
//
//    fun deleteNote(entity: Notes){
//        val userDao = NotesDatabase.getInstance(getApplication())!!.noteDao()
//        userDao.deleteNotes(entity)
//        getAllNote()
//    }
//
//    fun updateNote(entity: Notes){
//        val userDao = NotesDatabase.getInstance(getApplication())!!.noteDao()
//        userDao.updateNotes(entity)
//        getAllNote()
//    }
}