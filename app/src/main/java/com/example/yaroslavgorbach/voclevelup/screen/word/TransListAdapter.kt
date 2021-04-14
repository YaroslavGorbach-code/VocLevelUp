package com.example.yaroslavgorbach.voclevelup.screen.word

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.databinding.ItemTransBinding
import com.example.yaroslavgorbach.voclevelup.util.inflateBinding

class TransListAdapter : ListAdapter<String, TransListAdapter.TransVh>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = true
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransVh(parent.inflateBinding(ItemTransBinding::inflate))

    override fun onBindViewHolder(holder: TransVh, position: Int) = holder.bind(getItem(position))

    inner class TransVh(private val binding: ItemTransBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(trans: String) = with(binding) {
            transText.text = trans
        }
    }
}