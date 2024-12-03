package com.example.banksampah.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.banksampah.DB_USERS
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
import com.example.banksampah.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedpref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedpref = requireContext().getSharedPreferences(SHAREDPREF_USERS, Context.MODE_PRIVATE)

        val fullname = sharedpref.getString(SHAREDPREF_FULLNAME, "")
        val address = sharedpref.getString(SHAREDPREF_ADDRESS, "")
        val email = sharedpref.getString(SHAREDPREF_EMAIL, "")
        val telp = sharedpref.getString(SHAREDPREF_TELP, "")

        binding.editNama.setText(fullname)
        binding.editAlamat.setText(address)
        binding.editEmail.setText(email)
        binding.editNotelp.setText(telp)

        binding.btnEditprofile.setOnClickListener {
            val fullname = binding.editNama.text.toString()
            val address = binding.editAlamat.text.toString()
            val email = binding.editEmail.text.toString()
            val telp = binding.editNotelp.text.toString()

            val updateMap = mapOf(
                "fullname" to fullname,
                "address" to address,
                "email" to email,
                "telp" to telp
            )
            val mDb = FirebaseFirestore.getInstance()

            mDb.collection(DB_USERS).document(sharedpref.getString(SHAREDPREF_UIDDOCS, "").toString()).update(updateMap).addOnSuccessListener {

                //add ke sharedpref
                val addUser = sharedpref.edit()
                addUser.putString(SHAREDPREF_FULLNAME, fullname)
                addUser.putString(SHAREDPREF_TELP, telp)
                addUser.putString(SHAREDPREF_ADDRESS, address)
                addUser.putString(SHAREDPREF_EMAIL, email)
                addUser.apply()

                binding.progressBar.visibility = View.INVISIBLE

                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)

            }.addOnFailureListener {
                binding.progressBar.visibility = View.INVISIBLE
                Log.e("error auth", "${it.message} ${it.stackTrace}")
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
}