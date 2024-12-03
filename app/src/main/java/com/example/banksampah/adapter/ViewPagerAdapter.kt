package com.example.banksampah.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.banksampah.fragment.riwayat.admin.RiwayatPengambilanAdminFragment
import com.example.banksampah.fragment.riwayat.warga.RiwayatPengambilanFragment
import com.example.banksampah.fragment.riwayat.admin.RiwayatPenukaranAdminFragment
import com.example.banksampah.fragment.riwayat.warga.RiwayatPenukaranFragment
import com.example.banksampah.model.Users

class ViewPagerRiwayatAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> RiwayatPengambilanFragment()
            else -> RiwayatPenukaranFragment()
        }
    }
}

class ViewPagerRiwayatAdminAdapter(fragmentActivity: FragmentActivity, private val getData: Users)
    : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> RiwayatPengambilanAdminFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("warga", getData)
                }
            }

            else -> RiwayatPenukaranAdminFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("warga", getData)
                }
            }
        }
    }
}
