package com.example.yaroslavgorbach.voclevelup.screen.word

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.WordDetails
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentWordBinding
import com.example.yaroslavgorbach.voclevelup.nav
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class WordFragment : Fragment(R.layout.fragment_word) {

    interface Host {
        fun onWordNotFound(text: String)
    }

    companion object Args {
        fun argsOf(word: Word) = bundleOf("word" to word.text)
        private val WordFragment.wordText get() = requireArguments()["word"] as String
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<WordViewModel>()
        val binding = FragmentWordBinding.bind(view)
        with(vm.wordDetails(wordText)) {
            val listAdapter = TransListAdapter()
            binding.wordTransList.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(context)
            }
            details.observe(viewLifecycleOwner) {
                binding.wordProgress.isVisible = it is WordDetails.State.Loading
                binding.wordDetails.isVisible = it is WordDetails.State.Data
                when (it) {
                    WordDetails.State.Error -> (activity as Host).onWordNotFound(wordText)
                    is WordDetails.State.Data -> listAdapter.submitList(it.word.translations)
                }
            }
            word.observe(viewLifecycleOwner) {
                binding.wordText.text = it
            }
        }
        binding.wordUp.setOnClickListener { nav.up() }
    }
}