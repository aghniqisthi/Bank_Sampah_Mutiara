package com.example.banksampah.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.banksampah.DB_USERS
import com.example.banksampah.LoginActivity
import com.example.banksampah.NavBarActivity
import com.example.banksampah.R
import com.example.banksampah.SHAREDPREF_ADDRESS
import com.example.banksampah.SHAREDPREF_EMAIL
import com.example.banksampah.SHAREDPREF_FULLNAME
import com.example.banksampah.SHAREDPREF_ROLES
import com.example.banksampah.SHAREDPREF_TELP
import com.example.banksampah.SHAREDPREF_UID
import com.example.banksampah.SHAREDPREF_UIDDOCS
import com.example.banksampah.SHAREDPREF_USERS
import com.example.banksampah.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedpref: SharedPreferences
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseFirestore.getInstance()
        sharedpref = requireContext().getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)

        val fullname = sharedpref.getString(SHAREDPREF_FULLNAME, "")
        val address = sharedpref.getString(SHAREDPREF_ADDRESS, "")
        val email = sharedpref.getString(SHAREDPREF_EMAIL, "")
        val telp = sharedpref.getString(SHAREDPREF_TELP, "")
        val roles = sharedpref.getString(SHAREDPREF_ROLES, "")

        binding.txtFullname.text = fullname
        binding.txtAddress.text = address
        binding.txtEmail.text = email
        binding.txtTelp.text = telp

        if(roles == "Admin") binding.btnEditprofile.visibility = View.GONE
        binding.btnEditprofile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.btnLogout.setOnClickListener {
            mAuth.signOut()

            val addUser = sharedpref.edit()
            addUser.clear().apply()

            Log.d("cek logout", sharedpref.getString(SHAREDPREF_UID, "").toString())
            binding.progressBar.visibility = View.INVISIBLE
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}