package com.example.yaroslavgorbach.voclevelup.screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.data.repo
import com.example.yaroslavgorbach.voclevelup.screen.words.WordsListAdapter

class WordsFragment : Fragment(R.layout.fragment_words) {

    interface Host {
        fun openWord(word: Word)
        fun openAddWord()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init words list
        val rv = view.findViewById<RecyclerView>(R.id.wordsList)
        val adapter = WordsListAdapter {
            (activity as Host).openWord(it)
        }
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        val loadingPb = view.findViewById<View>(R.id.wordsProgress)
        val emptyView = view.findViewById<View>(R.id.wordsEmpty)
        repo.getAllWords().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            emptyView.isVisible = it.isEmpty()
            loadingPb.isVisible = false
        }

        // init add word button
        val btn = view.findViewById<View>(R.id.wordsAdd)
        btn.setOnClickListener{
            (activity as Host).openAddWord()
        }
    }
}