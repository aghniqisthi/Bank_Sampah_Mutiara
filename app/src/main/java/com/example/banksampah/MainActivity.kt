package com.example.banksampah

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.banksampah.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        supportActionBar?.hide()
        Glide.with(this).asGif().load(R.drawable.gif_splash).into(binding.imageView)

        sharedPref = this.getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)
        val uid = sharedPref.getString(SHAREDPREF_UID, "")

        Log.d("uid", uid.toString())
        Handler().postDelayed({
            val intent = Intent(this,
                if (uid == "") LoginActivity::class.java else NavBarActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}