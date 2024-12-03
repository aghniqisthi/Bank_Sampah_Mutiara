package com.example.banksampah.fragment.riwayat.admin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.SHAREDPREF_USERS
import com.example.banksampah.adapter.RiwayatPenukaranAdapter
import com.example.banksampah.adapter.ViewModelPenukaran
import com.example.banksampah.convertDate
import com.example.banksampah.databinding.FragmentRiwayatPenukaranAdminBinding
import com.example.banksampah.model.PenukaranSampah
import com.example.banksampah.model.Users
import com.google.firebase.Timestamp
import java.text.DecimalFormat

class RiwayatPenukaranAdminFragment : Fragment() {
    private var _binding: FragmentRiwayatPenukaranAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel: ViewModelPenukaran

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatPenukaranAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val getData = arguments?.getParcelable("warga") as Users?
        loadData(getData)
    }

    private fun loadData(data: Users?) {
        viewmodel.getIdAllPenukaranWarga(data!!.uId)
        viewmodel.getIdAllPenukaranObservers().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.recyclerviewRiwayatpenukaran.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.recyclerviewRiwayatpenukaran.adapter = RiwayatPenukaranAdapter(it.map {
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
                }, data, true)

                binding.imgEmptydata.visibility = View.INVISIBLE
                binding.txtEmptydata.visibility = View.INVISIBLE
                binding.swiperefresh.isRefreshing = false

                Log.d("cek data rvpenukaran", it.size.toString())

                var total = 0
                it.map {
                    if (it.data?.get("status") == "Hasil penukaran dapat diambil di Bank Sampah") {
                        total = total + it.data?.get("total_harga").toString().toInt()
                    }
                }

                binding.txtTotalsaldo.text = "Rp${DecimalFormat("#,###").format(total)}"

                if (it.isEmpty()) {
                    binding.imgEmptydata.visibility = View.VISIBLE
                    binding.txtEmptydata.visibility = View.VISIBLE
                }
            } else {
                binding.swiperefresh.isRefreshing = false
                Toast.makeText(requireContext(), "Data kosong", Toast.LENGTH_SHORT).show()
                binding.imgEmptydata.visibility = View.VISIBLE
                binding.txtEmptydata.visibility = View.VISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge()

        Glide.with(this).load(
            "https://cdn3d.iconscout.com/3d/premium/thumb/empty-box-9606753-7818459.png?f=webp"
        ).into(binding.imgEmptydata)

        viewmodel = ViewModelProvider(this)[ViewModelPenukaran::class.java]

        val getData = arguments?.getParcelable("warga") as Users?
        Log.d("cek getdata tukar", getData.toString())

        binding.fabAddpenukaran.setOnClickListener {
            val bundleData = Bundle()
            bundleData.putParcelable("warga", getData)
            Navigation.findNavController(view).navigate(
                R.id.action_detailRiwayatWargaFragment_to_addPenukaranSampahFragment, bundleData
            )
        }

        loadData(getData)

        binding.swiperefresh.setOnRefreshListener {
            Log.i("LOG_TAG", "onRefreshTUKAR called from SwipeRefreshLayout")
            loadData(getData)
        }
    }
}