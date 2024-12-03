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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.SHAREDPREF_USERS
import com.example.banksampah.adapter.RiwayatPengambilanAdapter
import com.example.banksampah.adapter.ViewModelPengambilan
import com.example.banksampah.adapter.ViewModelPenukaran
import com.example.banksampah.convertDate
import com.example.banksampah.databinding.FragmentRiwayatPengambilanAdminBinding
import com.example.banksampah.formattedTotal
import com.example.banksampah.model.PengambilanHasil
import com.example.banksampah.model.Users
import com.google.firebase.Timestamp

class RiwayatPengambilanAdminFragment : Fragment() {
    private var _binding: FragmentRiwayatPengambilanAdminBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: ViewModelPengambilan by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatPengambilanAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadData(getData: Users?) {
        val viewmodelPenukaran = ViewModelProvider(this)[ViewModelPenukaran::class.java]

        viewmodel.getIdPengambilan(getData?.uId.toString())
        viewmodel.getIdPengambilanObservers().observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("cek", "${it.size} ; ${getData?.uId}")
                binding.recyclerviewRiwayatpengambilan.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.recyclerviewRiwayatpengambilan.adapter =
                    RiwayatPengambilanAdapter(it.map {
                        PengambilanHasil(
                            uId = it.data?.get("uid").toString(),
                            idD = it.id,
                            masuk_kas = it.data?.get("masuk_kas").toString().toInt(),
                            persentase_kas = it.data?.get("persentase_kas").toString().toInt()?:0,
                            saldo_diambil_akhir = it.data?.get("saldo_diambil_akhir").toString().toInt()?:0,
                            saldo_diambil_awal = it.data?.get("saldo_diambil_awal").toString().toInt()?:0,
                            sisa_saldo = it.data?.get("sisa_saldo").toString().toInt()?:0,
                            tanggal_pengambilan = convertDate(it.data?.get("tanggal_pengambilan") as Timestamp),
                        )
                    }, getData!!, true)

                binding.imgEmptydata.visibility = View.INVISIBLE
                binding.txtEmptydata.visibility = View.INVISIBLE
                Log.d("cek data", it.size.toString())

                var totalSaldoDiambil = 0
                it.map {
                    totalSaldoDiambil += (it.data?.get("saldo_diambil_akhir").toString()
                        .toIntOrNull() ?: 0) + (it.data?.get("masuk_kas").toString().toIntOrNull()
                        ?: 0)
                }

                viewmodelPenukaran.getIdAllPenukaranWarga(getData.uId)
                viewmodelPenukaran.getIdAllPenukaranObservers()
                    .observe(viewLifecycleOwner) { penukaran ->

                        binding.swiperefresh.isRefreshing = false

                        var totalSaldoKeseluruhan = 0
                        penukaran.map { its ->
                            if (its.data?.get("status") == "Hasil penukaran dapat diambil di Bank Sampah") {
                                totalSaldoKeseluruhan += its.data?.get("total_harga").toString()
                                    .toInt()
                            }
                        }
                        Log.d("cek total", "all ${totalSaldoKeseluruhan} ; ambil ${totalSaldoDiambil} ; ttl ${totalSaldoKeseluruhan - totalSaldoDiambil}")
                        binding.txtTotalsaldo.text = "Rp${formattedTotal(totalSaldoKeseluruhan - totalSaldoDiambil)}"
                    }

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

    override fun onResume() {
        super.onResume()
        val getData = arguments?.getParcelable("warga") as Users?
        loadData(getData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge()

        Glide.with(this).load(
            "https://cdn3d.iconscout.com/3d/premium/thumb/empty-box-9606753-7818459.png?f=webp"
        ).into(binding.imgEmptydata)

        val getData = arguments?.getParcelable("warga") as Users?

        binding.fabAddpengambilan.setOnClickListener {
            val bundleData = Bundle()
            bundleData.putParcelable("warga", getData)
            Navigation.findNavController(view).navigate(
                R.id.action_detailRiwayatWargaFragment_to_addPengambilanFragment, bundleData
            )
        }

        loadData(getData)

        binding.swiperefresh.setOnRefreshListener {
            Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout")
            loadData(getData)
        }
    }
}