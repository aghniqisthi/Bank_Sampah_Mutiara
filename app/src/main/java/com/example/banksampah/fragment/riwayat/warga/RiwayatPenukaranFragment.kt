package com.example.banksampah.fragment.riwayat.warga

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.banksampah.SHAREDPREF_ADDRESS
import com.example.banksampah.SHAREDPREF_EMAIL
import com.example.banksampah.SHAREDPREF_FULLNAME
import com.example.banksampah.SHAREDPREF_ROLES
import com.example.banksampah.SHAREDPREF_TELP
import com.example.banksampah.SHAREDPREF_UID
import com.example.banksampah.SHAREDPREF_USERS
import com.example.banksampah.adapter.RiwayatPenukaranAdapter
import com.example.banksampah.adapter.ViewModelPenukaran
import com.example.banksampah.convertDate
import com.example.banksampah.databinding.FragmentRiwayatPenukaranBinding
import com.example.banksampah.model.PenukaranSampah
import com.example.banksampah.model.Users
import com.google.firebase.Timestamp

class RiwayatPenukaranFragment : Fragment() {
    private var _binding: FragmentRiwayatPenukaranBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel : ViewModelPenukaran
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRiwayatPenukaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge()

        Glide.with(this).load(
            "https://cdn3d.iconscout.com/3d/premium/thumb/empty-box-9606753-7818459.png?f=webp"
        ).into(binding.imgEmptydata)

        sharedPreferences = requireContext().getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)
        viewmodel = ViewModelProvider(this)[ViewModelPenukaran::class.java]

        showData()

        binding.swiperefresh.setOnRefreshListener {
            Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout")
            showData()
        }

    }

    private fun showData() {
        viewmodel.getIdAllPenukaranWarga(sharedPreferences.getString(SHAREDPREF_UID, "").toString())
        viewmodel.getIdAllPenukaranObservers().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.recyclerviewRiwayat.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.recyclerviewRiwayat.adapter = RiwayatPenukaranAdapter(it.map {
                    PenukaranSampah(
                        uId = it.data?.get("uid").toString(),
                        idD = it.id,
                        total_harga = it.data?.get("total_harga").toString().toInt(),
                        berat = it.data?.get("berat").toString().toInt(),
                        harga_sampah = it.data?.get("harga_sampah").toString().toInt(),
                        jenis_sampah = it.data?.get("jenis_sampah").toString(),
                        status = it.data?.get("status").toString(),
                        tanggal_penukaran = convertDate(it.data?.get("tanggal_penukaran") as Timestamp),
                    )
                }, Users(
                    uId = sharedPreferences.getString(SHAREDPREF_UID, "").toString(),
                    fullname =  sharedPreferences.getString(SHAREDPREF_FULLNAME, "").toString(),
                    telp = sharedPreferences.getString(SHAREDPREF_TELP, "").toString(),
                    address = sharedPreferences.getString(SHAREDPREF_ADDRESS, "").toString(),
                    email = sharedPreferences.getString(SHAREDPREF_EMAIL, "").toString(),
                    roles = sharedPreferences.getString(SHAREDPREF_ROLES, "").toString(),
                    password = null
                ), false)
                binding.imgEmptydata.visibility = View.INVISIBLE
                binding.txtEmptydata.visibility = View.INVISIBLE

                if(it.isEmpty()) {
                    binding.imgEmptydata.visibility = View.VISIBLE
                    binding.txtEmptydata.visibility = View.VISIBLE
                }

                binding.swiperefresh.isRefreshing = false

            } else {
                binding.swiperefresh.isRefreshing = false
                Toast.makeText(requireContext(), "There is no data to show", Toast.LENGTH_SHORT).show()
                binding.imgEmptydata.visibility = View.VISIBLE
                binding.txtEmptydata.visibility = View.VISIBLE
            }
        }
    }

}