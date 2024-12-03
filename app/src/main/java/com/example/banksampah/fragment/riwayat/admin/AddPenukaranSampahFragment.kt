package com.example.banksampah.fragment.riwayat.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.banksampah.model.PenukaranSampah
import com.example.banksampah.R
import com.example.banksampah.model.Users
import com.example.banksampah.databinding.FragmentAddPenukaranSampahBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat


class AddPenukaranSampahFragment : Fragment() {

    private var _binding: FragmentAddPenukaranSampahBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPenukaranSampahBinding.inflate(inflater, container, false)
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
        val data = arguments?.getParcelable("dataSampah") as PenukaranSampah?

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        if (data != null) binding.toolbar.setTitle("Edit Status")
        else binding.toolbar.setTitle("Tambah Data Penukaran")

        binding.editNama.setText(getData?.fullname.toString())

        val milliseconds = Timestamp.now().seconds * 1000 + Timestamp.now().nanoseconds / 1000000
        val netDate = Date(milliseconds)
        val date = SimpleDateFormat("dd MMMM yyyy").format(netDate).toString()
        binding.editTanggal.setText(date)

        val status = resources.getStringArray(R.array.status)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, status)
        binding.editStatus.setAdapter(arrayAdapter)

        if (data != null) {
//            Log.d("cek dataId", data.jenis_sampah)
//
//            FirebaseFirestore.getInstance()
//                .collection("penukaran-sampah")
//                .document(data.idD.toString())
//                .get()
//                .addOnSuccessListener {
//                    if (it != null) {
            binding.editJenis.setText(data.jenis_sampah)
            binding.editJenis.isEnabled = false

            binding.editHarga.setText(data.harga_sampah.toString())
            binding.editHarga.isEnabled = false

            binding.editBerat.setText(data.berat.toString())
            binding.editBerat.isEnabled = false

            val a = data.berat
            val b = data.harga_sampah
            formattedTotal(a * b)

            binding.btnAdddata.setText("Ubah Status")

            binding.btnAdddata.setOnClickListener { a ->
                FirebaseFirestore.getInstance()
                    .collection("penukaran-sampah")
                    .document(data.idD.toString())
                    .update("status", binding.editStatus.text.toString())
                    .addOnSuccessListener {
                        binding.progressBar.visibility = View.INVISIBLE
                        Snackbar.make(binding.main, "Data berhasil diubah", Snackbar.LENGTH_LONG).show()
                        findNavController().popBackStack()

//                                    val bundle = Bundle()
//                                    bundle.putParcelable("warga", getData)
//                                    Navigation.findNavController(view).navigate(
//                                        R.id.action_addPenukaranSampahFragment_to_detailRiwayatWargaFragment,
//                                        bundle,
//                                        NavOptions.Builder()
//                                            .setPopUpTo(R.id.addPenukaranSampahFragment, true)
//                                            .build()
//                                    )

//                                    Navigation.findNavController(view)
//                                        .navigate(R.id.action_addPenukaranSampahFragment_to_detailRiwayatWargaFragment)
                    }
                    .addOnFailureListener {
                        binding.progressBar.visibility = View.INVISIBLE
                        Log.e("error firestore", "${it.message} ${it.stackTrace}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }

            }
//                    } else {
//                        Log.d("empty getdatabyid", "${it}")
//                    }
//                }
//                .addOnFailureListener {
//                    Log.e("error getdatabyid", "${it.message} ${it.stackTrace}")
//                }
        } else {
            binding.editBerat.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    formattedTotal(calculateTotal())
                }
            })

            binding.editHarga.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    formattedTotal(calculateTotal())
                }
            })
            binding.btnAdddata.setOnClickListener {
                Log.d("cek status", binding.editStatus.text.toString())

                val documentRef =
                    FirebaseFirestore.getInstance().collection("penukaran-sampah").document()
                documentRef.set(
                    PenukaranSampah(
                        uId = getData?.uId.toString(),
                        idD = documentRef.id,
                        total_harga = calculateTotal(),
                        berat = binding.editBerat.text.toString().toInt(),
                        harga_sampah = binding.editHarga.text.toString().toInt(),
                        jenis_sampah = binding.editJenis.text.toString(),
                        status = binding.editStatus.text.toString(),
                        tanggal_penukaran = Timestamp.now().toDate(),
                    )
                )
                    .addOnSuccessListener {
                        binding.progressBar.visibility = View.INVISIBLE
                        Snackbar.make(
                            binding.main,
                            "Data berhasil ditambahkan",
                            Snackbar.LENGTH_LONG
                        ).show()
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

    private fun calculateTotal(): Int {
        val berat = binding.editBerat.text.toString().toIntOrNull() ?: 0
        val harga = binding.editHarga.text.toString().toIntOrNull() ?: 0
        return berat * harga
    }

    private fun formattedTotal(total: Int) {
        val formattedTotal = DecimalFormat("#,###").format(total)
        binding.txtTotal.text = "Rp$formattedTotal"
    }
}