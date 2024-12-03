package com.example.banksampah.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.SHAREDPREF_FULLNAME
import com.example.banksampah.SHAREDPREF_USERS
import com.example.banksampah.adapter.ViewModelPengambilan
import com.example.banksampah.adapter.ViewModelPenukaran
import com.example.banksampah.adapter.ViewModelWarga
import com.example.banksampah.databinding.FragmentHomeAdminBinding
import com.example.banksampah.formattedTotal
import com.google.firebase.firestore.FirebaseFirestore

class HomeAdminFragment : Fragment() {
    private var _binding: FragmentHomeAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedpref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).asGif().load(R.drawable.img_bank_sampah).into(binding.imageView2)
        requireActivity().enableEdgeToEdge()

        val viewmodelPenukaran = ViewModelProvider(this)[ViewModelPenukaran::class.java]
        val viewmodelPengambilan = ViewModelProvider(this)[ViewModelPengambilan::class.java]
        val viewmodelWarga = ViewModelProvider(this)[ViewModelWarga::class.java]
        sharedpref = requireContext().getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)

        binding.txtFullname.setText(sharedpref.getString(SHAREDPREF_FULLNAME, "").toString())

        FirebaseFirestore.getInstance().collection("tahun").get()
            .addOnSuccessListener {
                if (it != null) {
                    val tahun = it.documents.map {
                        it.id
                    }
                    val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, tahun)
                    binding.editTahun.setAdapter(arrayAdapter)
                } else {
                    Log.d("empty getallwarga", "${it}")
                }
            }
            .addOnFailureListener {
                Log.e("error getallwarga", "${it.message} ${it.stackTrace}")
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }

        binding.editTahun.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("cek tahun", binding.editTahun.text.toString())
            viewmodelWarga.getTotalPenukaranObservers(binding.editTahun.text.toString())
                .observe(viewLifecycleOwner) {
                    if (it != null) {
                        binding.txtTotalpenukar.text = it[0].toString()
                        binding.txtSampahditerima.text = it[1].toString()
                        binding.txtSampahberhasil.text = it[2].toString()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "There is no data to show",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

            viewmodelWarga.getTotalKasObservers(binding.editTahun.text.toString()).observe(viewLifecycleOwner){
                binding.txtTotalmasukkas.text = "Rp${formattedTotal(it)}"
            }
        }


        viewmodelPengambilan.getAllPengambilanObservers().observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("cek datazz", it.size.toString())

                var totalSaldoDiambil = 0
                var totalKas = 0
                it.map {
                    totalSaldoDiambil = totalSaldoDiambil + (it.data?.get("saldo_diambil_akhir")
                        .toString().toIntOrNull() ?: 0)
                    totalKas = totalKas + (it.data?.get("masuk_kas").toString().toIntOrNull() ?: 0)
                }
                binding.txtSaldotelahdiambil.text = "Rp${formattedTotal(totalSaldoDiambil)}"

                viewmodelPenukaran.getAllPenukaranObservers()
                    .observe(viewLifecycleOwner) { penukaran ->

                        var totalSaldoKeseluruhan = 0
                        penukaran.map { its ->
                            if (its.data?.get("status") == "Hasil penukaran dapat diambil di Bank Sampah") {
                                totalSaldoKeseluruhan = totalSaldoKeseluruhan + (its.data?.get("total_harga")
                                    .toString().toIntOrNull() ?: 0)
                            }
                        }
                        Log.d("cek saldo", "${totalSaldoDiambil} ${totalSaldoKeseluruhan}")

                        binding.txtSaldoblmdiambil.text =
                            "Rp${formattedTotal(totalSaldoKeseluruhan - totalSaldoDiambil - totalKas)}"
                    }

                if (it.isEmpty()) {
                    Toast.makeText(requireContext(), "Data kosong", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Data kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}