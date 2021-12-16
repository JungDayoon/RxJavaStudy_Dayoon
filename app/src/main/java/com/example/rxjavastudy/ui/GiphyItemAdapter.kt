package com.example.rxjavastudy.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rxjavastudy.data.Giphy
import com.example.rxjavastudy.databinding.GiphyItemLayoutBinding

class GiphyItemAdapter : RecyclerView.Adapter<GiphyItemAdapter.ViewHolder>() {
    val giphyList: ArrayList<Giphy?> = arrayListOf()

    inner class ViewHolder(
        private val binding: GiphyItemLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Log.d("giphyTest", "url: ${giphyList[position]?.images?.fixed_height?.url}")
            Glide.with(binding.root)
                .asGif()
                .load(giphyList[position]?.images?.fixed_height?.url)
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
        return giphyList.size
    }

    fun bindData(item: Giphy?) {
        giphyList.add(item)
        Log.d("giphyTest", "adapter giphyList size: $itemCount")
        notifyItemInserted(giphyList.size - 1)
    }
}
