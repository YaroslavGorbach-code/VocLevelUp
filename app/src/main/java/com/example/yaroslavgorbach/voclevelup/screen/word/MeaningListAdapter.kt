package com.example.yaroslavgorbach.voclevelup.screen.word

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.databinding.ItemMeanBinding
import com.example.yaroslavgorbach.voclevelup.util.inflateBinding

class MeaningListAdapter : ListAdapter<String, MeaningListAdapter.MeanVh>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = true
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MeanVh(parent.inflateBinding(ItemMeanBinding::inflate))

    override fun onBindViewHolder(holder: MeanVh, position: Int) = holder.bind(getItem(position))

    inner class MeanVh(private val binding: ItemMeanBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meaning: String) = with(binding) {
            meanText.text = meaning
        }
    }
}