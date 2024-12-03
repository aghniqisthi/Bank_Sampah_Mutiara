package com.example.banksampah

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.banksampah.databinding.ActivityRegisterBinding
import com.example.banksampah.model.Users
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseFirestore.getInstance()

        binding.btnRegis.setOnClickListener {

            val fullname = binding.editNama.text.toString()
            val telp = binding.editNotelp.text.toString()
            val address = binding.editAlamat.text.toString()
            val email = binding.editEmail.text.toString()
            val pass = binding.editPassword.text.toString()
            val confPass = binding.editConfpassword.text.toString()

            Log.d("check data", "$pass $email")

            binding.progressBar.visibility = View.VISIBLE

            if (pass.equals(confPass) && pass != "" && confPass != "") {
                mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener {

                    val user = mAuth.currentUser!!
                    mDb.collection(DB_USERS).document()
                        .set(Users(user.uid, fullname, telp, address, email, pass, "Warga"))
                        .addOnSuccessListener {
                            binding.progressBar.visibility = View.INVISIBLE

                            Snackbar.make(binding.main, "Pendaftaran akun berhasil", Snackbar.LENGTH_LONG)
                                .show()

                            val intent = Intent(this, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            binding.progressBar.visibility = View.INVISIBLE
                            Log.e("error firestore", "${it.message} ${it.stackTrace}")
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }

                }.addOnFailureListener {
                    binding.progressBar.visibility = View.INVISIBLE
                    Log.e("error auth", "${it.message} ${it.stackTrace}")
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                Snackbar.make(binding.main, "Password tidak sesuai", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}