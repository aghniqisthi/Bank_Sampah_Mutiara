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
import com.example.banksampah.databinding.CardRiwayatPengambilanBinding
import com.example.banksampah.model.Users
import com.example.banksampah.databinding.CardRiwayatPenukaranBinding
import com.example.banksampah.model.PengambilanHasil
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Date

class RiwayatPengambilanAdapter(
    private var listRiwayat: List<PengambilanHasil>,
    private var dataWarga: Users,
    private var isAdmin: Boolean
) : ListAdapter<PengambilanHasil, RiwayatPengambilanAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(var binding: CardRiwayatPengambilanBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardRiwayatPengambilanBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = SimpleDateFormat("dd MMMM yyyy").format(listRiwayat[position].tanggal_pengambilan).toString()

        holder.binding.txtTanggal.setText(date)
        holder.binding.txtDanaDiambil.setText(listRiwayat[position].saldo_diambil_akhir.toString())
        holder.binding.txtDana.setText(
            "Dana Kas: ${listRiwayat[position].saldo_diambil_awal} x ${
                listRiwayat[position].persentase_kas
            }% = ${listRiwayat[position].masuk_kas}"
        )

        if (isAdmin == true) {
            holder.binding.btnEditData.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("warga", dataWarga)
                bundle.putParcelable("data", listRiwayat[position])
                bundle.putString("dataId", listRiwayat[position].idD)

                Navigation.findNavController(it).navigate(
                    R.id.action_detailRiwayatWargaFragment_to_addPengambilanFragment, bundle
                )
            }
        } else {
            holder.binding.btnEditData.visibility = View.GONE
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PengambilanHasil> =
            object : DiffUtil.ItemCallback<PengambilanHasil>() {

                override fun areItemsTheSame(
                    oldUser: PengambilanHasil,
                    newUser: PengambilanHasil
                ): Boolean = oldUser.idD == newUser.idD

                override fun areContentsTheSame(
                    oldUser: PengambilanHasil,
                    newUser: PengambilanHasil
                ): Boolean = oldUser.idD == newUser.idD

            }
    }

    override fun getItemCount(): Int = listRiwayat.size

}