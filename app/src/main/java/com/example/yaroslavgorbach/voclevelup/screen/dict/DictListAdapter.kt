package com.example.yaroslavgorbach.voclevelup.screen.dict

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.ItemWordBinding

class DictListAdapter(
    private val onWordClick: (Word) -> Unit
) : ListAdapter<Word, DictListAdapter.WordVh>(object : DiffUtil.ItemCallback<Word>() {
    override fun areItemsTheSame(oldItem: Word, newItem: Word) = oldItem.trans == newItem.trans
    override fun areContentsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordVh {
        return WordVh(ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)) {
            onWordClick(getItem(it))
        }
    }

    override fun onBindViewHolder(holder: WordVh, position: Int) = holder.bind(getItem(position))

    class WordVh(private val binding: ItemWordBinding, onItemClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onItemClick(adapterPosition) }
        }

        fun bind(word: Word) = with(binding) {
            wordText.text = word.trans.text
            wordTransCount.text = root.resources.getString(
                R.string.meanings_count_pattern,
                word.trans.meanings.size
            )
        }
    }
}