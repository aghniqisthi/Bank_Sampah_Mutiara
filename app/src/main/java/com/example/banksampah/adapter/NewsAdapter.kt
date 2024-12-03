package com.example.banksampah.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.databinding.CardBeritaBinding
import com.example.banksampah.model.Article

class NewsAdapter(private var listNews: List<Article?>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardBeritaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardBeritaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(listNews[position]?.urlToImage).into(holder.binding.imgBerita)
        holder.binding.txtTitle.setText(listNews[position]?.title)
        holder.binding.txtPublisher.setText(listNews[position]?.author)
        holder.binding.txtDesc.setText(listNews[position]?.description)

        holder.binding.cardBerita.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(listNews[position]?.url))
            startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int = listNews.size

}