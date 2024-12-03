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
import com.example.banksampah.adapter.RiwayatPengambilanAdapter
import com.example.banksampah.adapter.ViewModelPengambilan
import com.example.banksampah.adapter.ViewModelPenukaran
import com.example.banksampah.convertDate
import com.example.banksampah.databinding.FragmentRiwayatPengambilanBinding
import com.example.banksampah.model.PengambilanHasil
import com.example.banksampah.model.Users
import com.google.firebase.Timestamp
import java.util.Date

class RiwayatPengambilanFragment : Fragment() {
    private var _binding: FragmentRiwayatPengambilanBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatPengambilanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge()

        Glide.with(this).load(
            "https://cdn3d.iconscout.com/3d/premium/thumb/empty-box-9606753-7818459.png?f=webp"
        ).into(binding.imgEmptydata)

        sharedPreferences =
            requireContext().getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)


        binding.swiperefresh.setOnRefreshListener {
            Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout")
            showData()
        }

        showData()
    }

    private fun showData() {
        val viewmodel = ViewModelProvider(this)[ViewModelPengambilan::class.java]

        viewmodel.getIdPengambilan(sharedPreferences.getString(SHAREDPREF_UID, "").toString())
        viewmodel.getIdPengambilanObservers().observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("cek", it.size.toString())
                binding.recyclerviewRiwayat.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.recyclerviewRiwayat.adapter = RiwayatPengambilanAdapter(
                    it.map {
                        PengambilanHasil(
                            uId = it.data?.get("uid").toString(),
                            idD = it.id,
                            masuk_kas = it.data?.get("masuk_kas").toString().toInt() ?: 0,
                            persentase_kas = it.data?.get("persentase_kas").toString().toInt() ?: 0,
                            saldo_diambil_akhir = it.data?.get("saldo_diambil_akhir").toString()
                                .toInt() ?: 0,
                            saldo_diambil_awal = it.data?.get("saldo_diambil_awal").toString()
                                .toInt() ?: 0,
                            sisa_saldo = it.data?.get("sisa_saldo").toString().toInt() ?: 0,
                            tanggal_pengambilan = convertDate(it.data?.get("tanggal_pengambilan") as Timestamp),
                        )
                    }, Users(
                        uId = sharedPreferences.getString(SHAREDPREF_UID, "").toString(),
                        fullname = sharedPreferences.getString(SHAREDPREF_FULLNAME, "").toString(),
                        telp = sharedPreferences.getString(SHAREDPREF_TELP, "").toString(),
                        address = sharedPreferences.getString(SHAREDPREF_ADDRESS, "").toString(),
                        email = sharedPreferences.getString(SHAREDPREF_EMAIL, "").toString(),
                        password = null,
                        roles = sharedPreferences.getString(SHAREDPREF_ROLES, "").toString(),
                    ), false
                )
                binding.imgEmptydata.visibility = View.INVISIBLE
                binding.txtEmptydata.visibility = View.INVISIBLE

                if (it.isEmpty()) {
                    binding.imgEmptydata.visibility = View.VISIBLE
                    binding.txtEmptydata.visibility = View.VISIBLE
                }

                binding.swiperefresh.isRefreshing = false

            } else {
                Toast.makeText(requireContext(), "There is no data to show", Toast.LENGTH_SHORT)
                    .show()
                binding.imgEmptydata.visibility = View.VISIBLE
                binding.txtEmptydata.visibility = View.VISIBLE
                binding.swiperefresh.isRefreshing = false
            }
        }
    }


}
