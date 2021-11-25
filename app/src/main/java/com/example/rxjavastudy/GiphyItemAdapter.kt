package com.example.rxjavastudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rxjavastudy.databinding.GiphyItemLayoutBinding

class GiphyItemAdapter(
    private val items: ArrayList<GiphyItem>
): RecyclerView.Adapter<GiphyItemAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: GiphyItemLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Glide.with(binding.root)
                .load(items[position].url)
                .into(binding.giphyImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GiphyItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
