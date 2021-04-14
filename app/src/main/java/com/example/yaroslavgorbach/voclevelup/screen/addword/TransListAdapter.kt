package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.databinding.ItemTransBinding
import com.example.yaroslavgorbach.voclevelup.util.inflateBinding

class TransListAdapter(
    private val onSave: (AddWord.TransItem) -> Unit,
    private val onRemove: (AddWord.TransItem) -> Unit
) :
    ListAdapter<AddWord.TransItem, TransListAdapter.TransVh>(
        object : DiffUtil.ItemCallback<AddWord.TransItem>() {
            override fun areItemsTheSame(oldItem: AddWord.TransItem, newItem: AddWord.TransItem) =
                oldItem.trans == newItem.trans

            override fun areContentsTheSame(
                oldItem: AddWord.TransItem,
                newItem: AddWord.TransItem
            ) =
                oldItem == newItem
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransVh(parent.inflateBinding(ItemTransBinding::inflate))

    override fun onBindViewHolder(holder: TransVh, position: Int) = holder.bind(getItem(position))

    inner class TransVh(private val binding: ItemTransBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.transBg.setOnClickListener {
                if (!binding.transSaved.isActivated) {
                    onSave(getItem(adapterPosition))
                } else {
                    onRemove(getItem(adapterPosition))
                }
            }
        }

        fun bind(item: AddWord.TransItem) = with(binding) {
            transText.text = item.trans.text
            transMeanings.text = item.trans.meanings.joinToString(separator = "\n") {
                root.resources.getString(R.string.trans_meaning_pattern, it)
            }
            transSaved.isActivated = item.saved
        }
    }
}