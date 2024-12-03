package com.example.banksampah

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.banksampah.databinding.ActivityNavBarBinding

class NavBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavBarBinding
    private lateinit var sharedpref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityNavBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        enableEdgeToEdge()

        sharedpref = this.getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)
        val role = sharedpref.getString(SHAREDPREF_ROLES, "")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        if(role == "Admin"){
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.navigation_admin)
            navHostFragment.navController.graph = graph

            binding.bottomNavigationView.inflateMenu(R.menu.menu_admin)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                Log.d("check destination navbar", "${destination.id} ${destination.label} ${role}")
                when (destination.id) {
                    R.id.homeAdminFragment, R.id.penukaranFragment, R.id.beritaFragment, R.id.profileFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.bottomNavigationView.visibility = View.GONE
                    }
                }
            }
        } else {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.navigation_warga)
            navHostFragment.navController.graph = graph

            binding.bottomNavigationView.inflateMenu(R.menu.menu_warga)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                Log.d("check destination navbar", "${destination.id} ${destination.label}")
                when (destination.id) {
                    R.id.homeFragment, R.id.beritaFragment, R.id.riwayatFragment, R.id.profileFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.bottomNavigationView.visibility = View.GONE
                    }
                }
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)

//            when(destination.id){
//                R.id.homeFragment, R.id.historiFragment, R.id.whistlistFragment, R.id.profileFragment ->{
//                    showBottomNav()
//                }
//                else -> hideBottomNav()
//            }

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        Log.d("cek uiddocs", sharedpref.getString(SHAREDPREF_UIDDOCS, "").toString())
//
//        navController.addOnDestinationChangedListener{ _, destination, _ ->
//            if (role == "Warga") {
//                when (destination.id) {
//                    R.id.homeFragment, R.id.beritaFragment, R.id.riwayatFragment, R.id.profileFragment -> {
//                        binding.bottomNavigationView.menu.findItem(R.id.menu_penukaran).isVisible = false
//                        binding.bottomNavigationView.menu.findItem(R.id.menu_riwayat).isVisible = true
//                        binding.bottomNavigationView.visibility = View.VISIBLE
//                    }
//
//                    else -> binding.bottomNavigationView.visibility = View.INVISIBLE
//                }
//            } else {
//                when (destination.id) {
//                    R.id.homeFragment, R.id.penukaranFragment, R.id.beritaFragment, R.id.profileFragment -> {
//                        binding.bottomNavigationView.menu.findItem(R.id.menu_penukaran).isVisible = true
//                        binding.bottomNavigationView.menu.findItem(R.id.menu_riwayat).isVisible = false
//                        binding.bottomNavigationView.visibility = View.VISIBLE
//                    }
//
//                    else -> binding.bottomNavigationView.visibility = View.INVISIBLE
//                }
//            }
//        }
//
//        binding.bottomNavigationView.setupWithNavController(navController)
//        Log.d("cek role", role.toString())

//        binding.bottomNavigationView.setOnItemSelectedListener {
//            when(it.itemId){
//                R.id.menu_home -> replaceFragment(HomeFragment())
//                R.id.menu_penukaran -> replaceFragment(PenukaranFragment())
//                R.id.menu_berita -> replaceFragment(BeritaFragment())
//                R.id.menu_profile -> replaceFragment(ProfileFragment())
//                R.id.menu_riwayat -> replaceFragment(RiwayatFragment())
//                else ->{
//                    Log.e("error else navbar", it.itemId.toString())
//                    replaceFragment(HomeFragment())
//                }
//            }
//            true
//        }

//    }

//    private fun replaceFragment(fragment: Fragment){
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit()
    }
}