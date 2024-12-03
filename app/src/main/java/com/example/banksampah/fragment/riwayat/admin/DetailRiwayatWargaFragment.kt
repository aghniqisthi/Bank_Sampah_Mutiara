package com.example.banksampah.fragment.riwayat.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.adapter.ViewModelPenukaran
import com.example.banksampah.adapter.ViewPagerRiwayatAdminAdapter
import com.example.banksampah.databinding.FragmentDetailRiwayatWargaBinding
import com.example.banksampah.model.Users
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailRiwayatWargaFragment : Fragment() {

    private var _binding: FragmentDetailRiwayatWargaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailRiwayatWargaBinding.inflate(inflater, container, false)
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

        val getData = arguments?.getParcelable("warga") as Users?

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitle(getData?.fullname.toString())
        binding.toolbar.setNavigationIcon(R.drawable.round_arrow_back_ios_24)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        val viewPager = binding.viewpagerRiwayat
        val tabLayout = binding.tabLayout
        viewPager.adapter = ViewPagerRiwayatAdminAdapter(requireActivity(), getData!!)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                1 -> {
                    tab.text = "Riwayat Pengambilan"
                    tab.setIcon(R.drawable.baseline_paid_24)
                }
                else -> {
                    tab.text = "Riwayat Penukaran"
                    tab.setIcon(R.drawable.rounded_grocery_24)
                }
            }
        }.attach()
    }
}