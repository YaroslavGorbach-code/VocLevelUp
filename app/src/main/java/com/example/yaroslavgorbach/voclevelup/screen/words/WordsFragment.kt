package com.example.yaroslavgorbach.voclevelup.screen.words

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.WordListImp
import com.example.yaroslavgorbach.voclevelup.component.WordList
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentWordsBinding
import com.example.yaroslavgorbach.voclevelup.nav

class WordsFragment : Fragment(R.layout.fragment_words) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<WordsViewModel>()
        val binding = FragmentWordsBinding.bind(view)

        with(vm.wordList) {
            val adapter = WordListAdapter(nav::openWord)
            binding.wordsList.adapter = adapter
            binding.wordsList.layoutManager = LinearLayoutManager(context)
            binding.wordsAdd.setOnClickListener { nav.openAddWord() }
            words.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding.wordsEmpty.isVisible = it.isEmpty()
            }
            loading.observe(viewLifecycleOwner) {
                binding.wordsProgress.isVisible = it
            }
        }
    }
}