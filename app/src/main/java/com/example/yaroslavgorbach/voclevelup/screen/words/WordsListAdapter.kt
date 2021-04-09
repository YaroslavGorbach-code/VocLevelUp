package com.example.yaroslavgorbach.voclevelup.screen.words

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.Word

class WordsListAdapter(
    private val onWordClick: (Word) -> Unit
) : ListAdapter<Word, WordsListAdapter.WordVh>(object : DiffUtil.ItemCallback<Word>() {
    override fun areItemsTheSame(oldItem: Word, newItem: Word) = oldItem.text == newItem.text
    override fun areContentsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WordVh(parent) {
        onWordClick(getItem(it))
    }

    override fun onBindViewHolder(holder: WordVh, position: Int) = holder.bind(getItem(position))

    class WordVh(parent: ViewGroup, onItemClick: (Int) -> Unit) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
    ) {

        private val text = itemView.findViewById<TextView>(R.id.wordText)

        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }

        fun bind(word: Word) {
            text.text = word.text
        }
    }
}