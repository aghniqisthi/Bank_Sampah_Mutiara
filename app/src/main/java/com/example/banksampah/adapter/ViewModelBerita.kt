package com.example.banksampah.adapter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.banksampah.model.ResponseDataBerita
import com.example.banksampah.model.RetrofitClientNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelBerita(app: Application) : AndroidViewModel(app) {

    var liveDataNews : MutableLiveData<ResponseDataBerita> = MutableLiveData()

    fun getliveDataNews() : MutableLiveData<ResponseDataBerita> {
        return liveDataNews
    }

    fun callApiNews(){
        RetrofitClientNews.instance.getAllNews().enqueue(object : Callback<ResponseDataBerita> {
            override fun onResponse(call: Call<ResponseDataBerita>, response: Response<ResponseDataBerita>) {
                if (response.isSuccessful){
                    liveDataNews.postValue(response.body())
                }
                else{

                }
            }
            override fun onFailure(call: Call<ResponseDataBerita>, t: Throwable) {

            }
        })
    }
}