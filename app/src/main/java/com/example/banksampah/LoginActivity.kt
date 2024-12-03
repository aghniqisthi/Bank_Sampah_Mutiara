package com.example.banksampah

import android.R.attr.bottom
import android.R.attr.left
import android.R.attr.right
import android.R.attr.top
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.setMargins
import com.example.banksampah.databinding.ActivityLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: FirebaseFirestore
    private lateinit var sharedpref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        supportActionBar?.hide()

        sharedpref = this.getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)
        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseFirestore.getInstance()

        binding.txtLupapass.setOnClickListener {

            val alertDialog = MaterialAlertDialogBuilder(this)
            alertDialog.setTitle("Masukkan email")

            val editText = EditText(this)
            val container = LinearLayout(this)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(24,24,24,24)
            editText.layoutParams = params
            container.addView(editText)

            alertDialog.setView(container)
            alertDialog.setPositiveButton("Ok") { dialog, whichButton ->
                if (editText.text.toString().isNullOrEmpty()) {
                    Toast.makeText(this, "Masukkan email anda", Toast.LENGTH_SHORT).show()
                } else {
                    mAuth.sendPasswordResetEmail(editText.text.toString().trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Link reset password telah dikirim! Periksa email anda untuk mengganti password",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }.setNegativeButton("Cancel", { dialog, _ ->
                dialog.dismiss()
            })
            alertDialog.show()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val pass = binding.editPassword.text.toString()

            binding.progressBar.visibility = View.VISIBLE

            if (email != "" && pass != "") {
                mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener {

                        val user = mAuth.currentUser!!

                        mDb.collection(DB_USERS)
                            .whereEqualTo("uid", FirebaseAuth.getInstance().currentUser!!.uid)
                            .get().addOnSuccessListener {
                                if (it != null) {
                                    Log.d(
                                        "cek fullnameee",
                                        it.documents.first().data?.get("fullname").toString()
                                    )

                                    //add ke sharedpref
                                    val addUser = sharedpref.edit()
                                    addUser.putString(SHAREDPREF_UID, user.uid)
                                    addUser.putString(SHAREDPREF_UIDDOCS, it.documents.first().id)
                                    addUser.putString(
                                        SHAREDPREF_FULLNAME,
                                        it.documents.first().data?.get("fullname").toString()
                                    )
                                    addUser.putString(
                                        SHAREDPREF_TELP,
                                        it.documents.first().data?.get("telp").toString()
                                    )
                                    addUser.putString(
                                        SHAREDPREF_ADDRESS,
                                        it.documents.first().data?.get("address").toString()
                                    )
                                    addUser.putString(SHAREDPREF_EMAIL, user.email)
                                    addUser.putString(
                                        SHAREDPREF_ROLES,
                                        it.documents.first().data?.get("roles").toString()
                                    )
                                    addUser.apply()

                                    val updateMap = mapOf("password" to pass)

                                    FirebaseFirestore.getInstance()
                                        .collection(DB_USERS)
                                        .document(
                                            sharedpref.getString(SHAREDPREF_UIDDOCS, "").toString()
                                        )
                                        .update(updateMap).addOnFailureListener {
                                            Log.e(
                                                "error edit pass",
                                                "${it.message.toString()} ${it.stackTrace}"
                                            )
                                            Toast.makeText(
                                                this,
                                                "Error: ${it.message}", Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    binding.progressBar.visibility = View.INVISIBLE

                                    val intent = Intent(this, NavBarActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                } else {
                                    Log.d("empty data store", "${it}")
                                    Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }.addOnFailureListener {
                        binding.progressBar.visibility = View.INVISIBLE
                        Log.e("error auth", "${it.message} ${it.stackTrace}")
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }

            } else {
                binding.progressBar.visibility = View.INVISIBLE
                Snackbar.make(binding.main, "Isi data dengan benar", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}