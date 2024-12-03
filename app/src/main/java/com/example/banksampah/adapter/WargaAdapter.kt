package com.example.banksampah.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.model.Users
import com.example.banksampah.databinding.CardWargaBinding

class WargaAdapter(private val listWarga: List<Users>) :
    ListAdapter<Users, WargaAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(var binding: CardWargaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardWargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(listWarga[position].roles != "Admin") {
            holder.binding.txtAddress.setText(listWarga[position].address)
            holder.binding.txtFullname.setText(listWarga[position].fullname)
            holder.binding.txtTelp.setText(listWarga[position].telp)

            holder.binding.cardWarga.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("warga", listWarga[position])
                Navigation.findNavController(it)
                    .navigate(R.id.action_penukaranFragment_to_detailRiwayatWargaFragment, bundle)
            }
        }

//        holder.binding.txtAddress.setText(listWarga[position].data?.get("address").toString())
//        holder.binding.txtFullname.setText(listWarga[position].data?.get("fullname").toString())
//        holder.binding.txtTelp.setText(listWarga[position].data?.get("telp").toString())
//
//        holder.binding.cardWarga.setOnClickListener {
//            val bundle = Bundle()
//            val user = Users(
//                uId = listWarga[position].data?.get("uid").toString(),
//                fullname = listWarga[position].data?.get("fullname").toString(),
//                telp = listWarga[position].data?.get("telp").toString(),
//                address = listWarga[position].data?.get("address").toString(),
//                email = listWarga[position].data?.get("email").toString(),
//                password = listWarga[position].data?.get("password").toString(),
//                roles = listWarga[position].data?.get("roles").toString(),
//            )
//            bundle.putParcelable("warga", user)
//            Navigation.findNavController(it)
//                .navigate(R.id.action_penukaranFragment_to_detailRiwayatWargaFragment, bundle)
//        }
    }

    override fun getItemCount(): Int {
        var total = 0
        listWarga.map {
            if(it.roles != "Admin") total++
        }
        return total
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Users> =
            object : DiffUtil.ItemCallback<Users>() {
                override fun areItemsTheSame(oldUser: Users, newUser: Users): Boolean {
                    return oldUser.uId == newUser.uId
                }

                override fun areContentsTheSame(oldUser: Users, newUser: Users): Boolean {
                    return oldUser == newUser
                }
            }
    }

}