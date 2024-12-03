package com.example.banksampah.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.R
import com.example.banksampah.adapter.NewsAdapter
import com.example.banksampah.adapter.ViewModelBerita
import com.example.banksampah.databinding.FragmentBeritaBinding
import com.example.banksampah.databinding.FragmentPenukaranBinding

class BeritaFragment : Fragment() {
    private var _binding: FragmentBeritaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel : ViewModelBerita

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBeritaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewmodel = ViewModelProvider(this)[ViewModelBerita::class.java]

        viewmodel.callApiNews()
        viewmodel.liveDataNews.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.recyclerviewBerita.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.recyclerviewBerita.adapter = NewsAdapter(it.articles!!)
            } else {
                Toast.makeText(requireContext(), "Data kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}