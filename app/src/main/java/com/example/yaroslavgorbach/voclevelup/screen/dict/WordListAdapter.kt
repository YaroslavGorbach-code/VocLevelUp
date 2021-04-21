package com.example.yaroslavgorbach.voclevelup.screen.dict

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.ItemWordBinding
import com.example.yaroslavgorbach.voclevelup.util.inflateBinding
import kotlin.random.Random

class WordListAdapter(
    private val onWordClick: (Word) -> Unit
) : ListAdapter<Word, WordListAdapter.WordVh>(object : DiffUtil.ItemCallback<Word>() {
    override fun areItemsTheSame(oldItem: Word, newItem: Word) = oldItem.text == newItem.text
    override fun areContentsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WordVh(parent.inflateBinding(ItemWordBinding::inflate))

    override fun onBindViewHolder(holder: WordVh, position: Int) = holder.bind(getItem(position))

    inner class WordVh(private val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onWordClick(getItem(adapterPosition)) }
        }

        fun bind(word: Word) = with(binding) {
            wordText.text = word.text
            wordTrans.text = word.translations.joinToString(separator = ", ")
            wordProgress.text = Random.nextInt(1, 100).toString()
        }
    }
}