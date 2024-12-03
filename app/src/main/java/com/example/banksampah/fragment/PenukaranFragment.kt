package com.example.banksampah.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.adapter.ViewModelWarga
import com.example.banksampah.adapter.WargaAdapter
import com.example.banksampah.databinding.FragmentPenukaranBinding
import com.example.banksampah.model.Users
import com.google.firebase.firestore.FirebaseFirestore

class PenukaranFragment : Fragment() {
    private var _binding: FragmentPenukaranBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel : ViewModelWarga

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPenukaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showData()

        binding.swiperefresh.setOnRefreshListener {
            Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout")
            showData()
        }

        binding.btnSearch.setOnClickListener {
            val data = binding.editTextbox.text.toString()
            if(data.isNotEmpty() && data.isNotBlank()){
                viewmodel = ViewModelProvider(this)[ViewModelWarga::class.java]
                viewmodel.searchWarga(data)
                viewmodel.getSearchWargaObservers().observe(viewLifecycleOwner){
                    if (it != null) {
                        binding.recyclerviewWarga.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        val users = it.map {
                            Users(
                                uId = it.data?.get("uid").toString(),
                                fullname = it.data?.get("fullname").toString(),
                                telp = it.data?.get("telp").toString(),
                                address = it.data?.get("address").toString(),
                                email = it.data?.get("email").toString(),
                                password = it.data?.get("password").toString(),
                                roles = it.data?.get("roles").toString(),
                            )
                        }
                        binding.recyclerviewWarga.adapter = WargaAdapter(users)
                    } else {
                        Toast.makeText(requireContext(), "There is no data to show", Toast.LENGTH_SHORT).show()
                    }
                }
            } else showData()
        }
    }

    override fun onStart() {
        super.onStart()
           showData()
    }

    override fun onResume() {
        super.onResume()
        showData()
    }

    private fun showData(){
        viewmodel = ViewModelProvider(this)[ViewModelWarga::class.java]

        viewmodel.getAllWargaObservers().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.recyclerviewWarga.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                val users = it.map {
                    Users(
                        uId = it.data?.get("uid").toString(),
                        fullname = it.data?.get("fullname").toString(),
                        telp = it.data?.get("telp").toString(),
                        address = it.data?.get("address").toString(),
                        email = it.data?.get("email").toString(),
                        password = it.data?.get("password").toString(),
                        roles = it.data?.get("roles").toString(),
                    )
                }
                binding.recyclerviewWarga.adapter = WargaAdapter(users)
                binding.swiperefresh.isRefreshing = false
            } else {
                binding.swiperefresh.isRefreshing = false
                Toast.makeText(requireContext(), "There is no data to show", Toast.LENGTH_SHORT).show()
            }
        }
    }
}