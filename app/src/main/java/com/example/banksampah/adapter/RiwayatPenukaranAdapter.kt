package com.example.banksampah.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.model.Users
import com.example.banksampah.databinding.CardRiwayatPenukaranBinding
import com.example.banksampah.model.PenukaranSampah
import java.text.SimpleDateFormat

class RiwayatPenukaranAdapter(
    private var listRiwayat: List<PenukaranSampah>,
    private var dataWarga: Users,
    private var isAdmin: Boolean
) : ListAdapter<PenukaranSampah, RiwayatPenukaranAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(var binding: CardRiwayatPenukaranBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            CardRiwayatPenukaranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = SimpleDateFormat("dd MMMM yyyy").format(listRiwayat[position].tanggal_penukaran).toString()

        holder.binding.txtTanggal.setText(date)
        holder.binding.txtJenisSampah.setText(listRiwayat[position].jenis_sampah)
        holder.binding.txtTotalSampah.setText(
            "${listRiwayat[position].berat} x Rp${listRiwayat[position].harga_sampah
            } = ${listRiwayat[position].total_harga}"
        )
        holder.binding.txtStatus.setText(listRiwayat[position].status)

        if(isAdmin == true){
        holder.binding.btnUbahStatus.setOnClickListener {
            val bundle = Bundle()
//                        val user = Users(
//                            uId = it.documents.first().data?.get("uid").toString(),
//                            fullname = it.documents.first().data?.get("fullname").toString(),
//                            telp = it.documents.first().data?.get("telp").toString(),
//                            address = it.documents.first().data?.get("address").toString(),
//                            email = it.documents.first().data?.get("email").toString(),
//                            roles = it.documents.first().data?.get("roles").toString(),
//                        )
            bundle.putParcelable("warga", dataWarga)
            bundle.putParcelable("dataSampah", listRiwayat[position])

            Navigation.findNavController(it).navigate(
                R.id.action_detailRiwayatWargaFragment_to_addPenukaranSampahFragment, bundle
            )
        }
        } else {
            holder.binding.btnUbahStatus.visibility = View.GONE
        }
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PenukaranSampah> =
            object : DiffUtil.ItemCallback<PenukaranSampah>() {

                override fun areItemsTheSame(
                    oldUser: PenukaranSampah,
                    newUser: PenukaranSampah
                ): Boolean = oldUser.idD == newUser.idD

                override fun areContentsTheSame(
                    oldUser: PenukaranSampah,
                    newUser: PenukaranSampah
                ): Boolean = oldUser.idD == newUser.idD

            }
    }

    override fun getItemCount(): Int = listRiwayat.size

}