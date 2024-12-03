package com.example.banksampah.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.SHAREDPREF_FULLNAME
import com.example.banksampah.SHAREDPREF_UID
import com.example.banksampah.SHAREDPREF_USERS
import com.example.banksampah.adapter.ViewModelPengambilan
import com.example.banksampah.adapter.ViewModelPenukaran
import com.example.banksampah.databinding.FragmentHomeBinding
import com.example.banksampah.databinding.FragmentPenukaranBinding
import com.example.banksampah.formattedTotal
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel: ViewModelPenukaran

    //    private lateinit var viewmodelNews : ViewModelBerita
    private lateinit var sharedpref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge()

        Glide.with(this).asGif().load(R.drawable.img_bank_sampah).into(binding.imageView2)

        val viewmodelAmbil = ViewModelProvider(this)[ViewModelPengambilan::class.java]
        viewmodel = ViewModelProvider(this)[ViewModelPenukaran::class.java]
        sharedpref = requireContext().getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)

        binding.txtFullname.setText(sharedpref.getString(SHAREDPREF_FULLNAME, "").toString())

        viewmodel.getIdAllPenukaranWarga(sharedpref.getString(SHAREDPREF_UID, "").toString())
        viewmodel.getIdAllPenukaranObservers().observe(viewLifecycleOwner) {

            if (it != null) {
                if (it.isEmpty()) {
//                    binding.txtTotalberat.text = "0"
                    binding.txtTotalharga.text = "Rp0"
                } else {
                    var totalBerat = 0
                    var totalHarga = 0

                    it.map {
//                        totalBerat = totalBerat + it.data?.get("berat").toString().toInt()
                        totalHarga = totalHarga + it.data?.get("total_harga").toString().toInt()
                        Log.d("cek totalberat", "$totalBerat ; $totalHarga")
                    }
//                    binding.txtTotalberat.text = totalBerat.toString()

                    viewmodelAmbil.getIdPengambilan(sharedpref.getString(SHAREDPREF_UID, "").toString())
                    viewmodelAmbil.getIdPengambilanObservers().observe(viewLifecycleOwner) {

                        Log.d("cek datazz", it.size.toString())

                        var totalSaldoDiambil = 0
                        var totalKas = 0

                        it.map {
                            totalSaldoDiambil = totalSaldoDiambil + (it.data?.get("saldo_diambil_akhir")
                                .toString().toIntOrNull() ?: 0)
                            totalKas = totalKas + (it.data?.get("masuk_kas")
                                .toString().toIntOrNull() ?: 0)
                        }

                        binding.txtSaldoblmdiambil.text =
                            "Rp${formattedTotal(totalHarga - totalSaldoDiambil - totalKas)}"
                        binding.txtSaldotelahdiambil.text = "Rp${formattedTotal(totalSaldoDiambil)}"
                        binding.txtTotalmasukkas.text = "Rp${formattedTotal(totalKas)}"
                    }
                    binding.txtTotalharga.text = "Rp${formattedTotal(totalHarga)}"
                }
            } else {
                Toast.makeText(requireContext(), "There is no data to show", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}