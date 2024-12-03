package com.example.banksampah.fragment.riwayat.warga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.banksampah.R
import com.example.banksampah.adapter.ViewPagerRiwayatAdapter
import com.example.banksampah.databinding.FragmentRiwayatBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RiwayatFragment : Fragment() {
    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge()

        binding.viewpagerRiwayat.setAdapter(ViewPagerRiwayatAdapter(requireActivity()));

        TabLayoutMediator(binding.tabLayout, binding.viewpagerRiwayat) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.text = "Riwayat Penukaran"
                    tab.setIcon(R.drawable.rounded_grocery_24)
                }
                1 -> {
                    tab.text = "Riwayat Pengambilan"
                    tab.setIcon(R.drawable.baseline_paid_24)
                }
            }
        }.attach()
    }

}