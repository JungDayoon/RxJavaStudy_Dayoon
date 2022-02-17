package com.example.rxjavastudy.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavastudy.data.Term
import com.example.rxjavastudy.databinding.AutoCompleteItemLayoutBinding

class AutoCompleteItemAdapter(
    val itemClickListener: View.OnClickListener
) : ListAdapter<Term, AutoCompleteItemAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(
        private val binding: AutoCompleteItemLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Term) {
            binding.root.text = item.name
            binding.root.setOnClickListener(itemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AutoCompleteItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Term>() {
            override fun areContentsTheSame(oldItem: Term, newItem: Term) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Term, newItem: Term) =
                oldItem.name == newItem.name
        }
    }
}
