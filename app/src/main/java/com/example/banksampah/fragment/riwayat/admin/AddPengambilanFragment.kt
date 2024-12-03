package com.example.banksampah.fragment.riwayat.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.banksampah.R
import com.example.banksampah.adapter.ViewModelPenukaran
import com.example.banksampah.databinding.FragmentAddPengambilanBinding
import com.example.banksampah.model.PengambilanHasil
import com.example.banksampah.model.Users
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

class AddPengambilanFragment : Fragment() {
    private var _binding: FragmentAddPengambilanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPengambilanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationIcon(R.drawable.round_arrow_back_ios_24)
        binding.toolbar.setNavigationOnClickListener { view ->
            findNavController().popBackStack()
        }

        val getData = arguments?.getParcelable("warga") as Users?
        val data = arguments?.getParcelable("data") as PengambilanHasil?
        val dataId = arguments?.getString("dataId")

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        if (dataId != null && dataId != null) binding.toolbar.setTitle("Ubah Data")
        else binding.toolbar.setTitle("Tambah Data Pengambilan")
        binding.editNama.setText(getData?.fullname.toString())

        val milliseconds = Timestamp.now().seconds * 1000 + Timestamp.now().nanoseconds / 1000000
        val netDate = Date(milliseconds)
        val date = SimpleDateFormat("dd MMMM yyyy").format(netDate).toString()
        binding.editTanggal.setText(date)

        var totalSaldo = 0
        var totalDanaMasukKas = 0
        var totalDanaDiambil = 0

        val viewmodel = ViewModelProvider(this)[ViewModelPenukaran::class.java]
        viewmodel.getIdAllPenukaranWarga(getData!!.uId)
        viewmodel.getIdAllPenukaranObservers().observe(viewLifecycleOwner) {
            it.forEach {
                if (it.get("status") == "Hasil penukaran dapat diambil di Bank Sampah") {
                    totalSaldo = totalSaldo + it.get("total_harga").toString().toInt()
                    Log.d("lewat sini g", totalSaldo.toString())
                }
            }
            if (dataId != null && dataId != null) {
                Log.d("cek dataId", data?.saldo_diambil_awal.toString())

//                FirebaseFirestore.getInstance()
//                    .collection("pengambilan-hasil")
//                    .document(dataId)
//                    .get()
//                    .addOnSuccessListener {
//                        if (it != null) {
//                            val saldoDiambilAwal = it.data?.get("saldo_diambil_awal").toString()
//                            val persentaseKas = it.data?.get("persentase_kas").toString()

                binding.editSaldo.setText(data?.saldo_diambil_awal.toString())
                binding.editKas.setText(data?.persentase_kas.toString())
                binding.txtTotalkas.setText(formattedTotal(calculateDanaMasukKas()))
                binding.txtTotal.setText(formattedTotal(data?.saldo_diambil_awal ?: 0))

//                FirebaseFirestore.getInstance()
//                    .collection("pengambilan-hasil")
//                    .get()
//                    .addOnSuccessListener {
//                        if(it!=null){
//                            it.documents.map {
//                                totalDanaMasukKas =
//                                    totalDanaMasukKas + (it.get("masuk_kas").toString().toIntOrNull()?:0)
//                                totalDanaDiambil =
//                                    totalDanaDiambil + (it.get("saldo_diambil_awal").toString().toIntOrNull()?:0)
//                            }
//                        }
//                    }.addOnFailureListener {
//                        Log.e("error getdatabyid", "${it.message} ${it.stackTrace}")
//                    }
//                binding.txtSisasaldo.setText(formattedTotal(totalSaldo - calculateDanaMasukKas() - totalDanaMasukKas - totalDanaDiambil))
                binding.btnAdddata.setText("Edit Data")

                binding.editSaldo.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        binding.txtTotalkas.setText(formattedTotal(calculateDanaMasukKas()))
                        binding.txtTotal.setText(
                            formattedTotal(binding.editSaldo.text.toString().toIntOrNull() ?: 0)
                        )
//                        binding.txtSisasaldo.setText(
//                            formattedTotal(
//                                totalSaldo - (binding.editSaldo.text.toString().toIntOrNull()
//                                    ?: 0) - calculateDanaMasukKas() - totalDanaMasukKas - totalDanaDiambil))
                    }
                })

                binding.editKas.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        binding.txtTotalkas.setText(formattedTotal(calculateDanaMasukKas()))
                        binding.txtTotal.setText(
                            formattedTotal(binding.editSaldo.text.toString().toIntOrNull() ?: 0)
                        )
//                        binding.txtSisasaldo.setText(
//                            formattedTotal(
//                                totalSaldo - (binding.editSaldo.text.toString().toIntOrNull()
//                                    ?: 0) - calculateDanaMasukKas()
//                            )
//                        )
                    }
                })

                binding.btnAdddata.setOnClickListener { z ->
                    Log.d("cek id", data?.saldo_diambil_awal.toString())
                    val updateMap = mapOf(
                        "masuk_kas" to calculateDanaMasukKas(),
                        "persentase_kas" to binding.editKas.text.toString(),
                        "saldo_diambil_akhir" to (binding.editSaldo.text.toString().toIntOrNull()
                            ?: 0),
                        "saldo_diambil_awal" to (binding.editSaldo.text.toString().toIntOrNull()
                            ?: 0),
                        "sisa_saldo" to (totalSaldo - (binding.editSaldo.text.toString()
                            .toIntOrNull() ?: 0) - calculateDanaMasukKas())
                    )

                    FirebaseFirestore.getInstance()
                        .collection("pengambilan-hasil")
                        .document(dataId)
                        .update(updateMap)
                        .addOnSuccessListener {
                            binding.progressBar.visibility = View.INVISIBLE
                            Snackbar.make(
                                binding.main,
                                "Data berhasil diubah",
                                Snackbar.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack()
                        }
                        .addOnFailureListener {
                            binding.progressBar.visibility = View.INVISIBLE
                            Log.e("error firestore", "${it.message} ${it.stackTrace}")
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                                .show()
                        }

                }
//                        } else {
//                            Log.d("empty getdatabyid", "${it}")
//                        }
//                    }
//                    .addOnFailureListener {
//                        Log.e("error getdatabyid", "${it.message} ${it.stackTrace}")
//                    }
            } else {
                binding.editSaldo.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        binding.txtTotalkas.setText(formattedTotal(calculateDanaMasukKas()))
                        binding.txtTotal.setText(
                            formattedTotal(binding.editSaldo.text.toString().toIntOrNull() ?: 0)
                        )
//                        binding.txtSisasaldo.setText(
//                            formattedTotal(
//                                totalSaldo - (binding.editSaldo.text.toString().toIntOrNull()
//                                    ?: 0) - calculateDanaMasukKas()
//                            )
//                        )
                    }
                })

                binding.editKas.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        binding.txtTotalkas.setText(formattedTotal(calculateDanaMasukKas()))
                        binding.txtTotal.setText(
                            formattedTotal(binding.editSaldo.text.toString().toIntOrNull() ?: 0)
                        )
//                        binding.txtSisasaldo.setText(
//                            formattedTotal(
//                                totalSaldo - (binding.editSaldo.text.toString().toIntOrNull()
//                                    ?: 0) - calculateDanaMasukKas()
//                            )
//                        )
                    }
                })

                binding.btnAdddata.setOnClickListener {
                    val documentRef =
                        FirebaseFirestore.getInstance().collection("pengambilan-hasil").document()

                    documentRef.set(
                        PengambilanHasil(
                            uId = getData.uId,
                            idD = documentRef.id,  // Set the idD to the document ID
                            masuk_kas = calculateDanaMasukKas(),
                            persentase_kas = binding.editKas.text.toString().toIntOrNull() ?: 0,
                            saldo_diambil_akhir = binding.editSaldo.text.toString()
                                .toIntOrNull() ?: 0,
                            saldo_diambil_awal = binding.editSaldo.text.toString().toIntOrNull()
                                ?: 0,
                            sisa_saldo =
                            totalSaldo - (binding.editSaldo.text.toString().toIntOrNull()
                                ?: 0) - calculateDanaMasukKas(),
                            tanggal_pengambilan = Timestamp.now().toDate()
                        )
                    )
                        .addOnSuccessListener {
                            binding.progressBar.visibility = View.INVISIBLE

                            Snackbar.make(
                                binding.main,
                                "Data berhasil ditambahkan",
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                            findNavController().popBackStack()
                        }
                        .addOnFailureListener {
                            binding.progressBar.visibility = View.INVISIBLE
                            Log.e("error firestore", "${it.message} ${it.stackTrace}")
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }

                }

            }
        }

    }

    private fun calculateDanaMasukKas(): Int {
        val saldoAwal = binding.editSaldo.text.toString().toIntOrNull() ?: 0
        val percentageKas = binding.editKas.text.toString().toIntOrNull() ?: 0
        return saldoAwal * percentageKas / 100
    }

    private fun formattedTotal(total: Int): String {
        return DecimalFormat("#,###").format(total)
    }
}