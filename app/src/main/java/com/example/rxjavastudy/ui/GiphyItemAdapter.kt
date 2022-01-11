package com.example.rxjavastudy.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rxjavastudy.R
import com.example.rxjavastudy.data.Gif
import com.example.rxjavastudy.databinding.GiphyItemLayoutBinding

class GiphyItemAdapter : ListAdapter<Gif, GiphyItemAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(
        private val binding: GiphyItemLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Gif?) {
            Log.d("giphyTest", "url: ${item?.images?.fixed_height?.url}")
            Glide.with(binding.root)
                .asGif()
                .placeholder(R.drawable.loading_icon)
                .load(item?.images?.fixed_height?.url)
                .into(binding.giphyImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GiphyItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Gif>() {
            override fun areContentsTheSame(oldItem: Gif, newItem: Gif) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Gif, newItem: Gif) =
                oldItem.id == newItem.id
        }
    }
}
