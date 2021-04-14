package com.example.yaroslavgorbach.voclevelup.screen.word

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yaroslavgorbach.voclevelup.component.WordDetails.*

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentWordBinding
import com.example.yaroslavgorbach.voclevelup.nav
import kotlinx.coroutines.ExperimentalCoroutinesApi

class WordFragment : Fragment(R.layout.fragment_word) {

    interface Host{
        fun onWordNotFound(text: String)
    }

    companion object Args {
        fun argsOf(word: Word) = bundleOf("word" to word.trans.text)
        private val WordFragment.wordText get() = requireArguments()["word"] as String
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<WordViewModel>()
        val binding = FragmentWordBinding.bind(view)

        with(vm.wordDetails(wordText)) {
            val listAdapter = MeaningListAdapter()
            binding.wordMeaningsList.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(context)
            }

            details.observe(viewLifecycleOwner) {
                binding.wordProgress.isVisible = it is State.Loading
                binding.wordDetails.isVisible = it is State.Data
                when (it) {
                    State.Error -> (activity as Host).onWordNotFound(wordText)
                    is State.Data -> listAdapter.submitList(it.word.trans.meanings)
                }
            }
            word.observe(viewLifecycleOwner) {
                binding.wordText.text = it
            }
        }
        binding.wordUp.setOnClickListener { nav.up() }
    }
}