package com.example.yaroslavgorbach.voclevelup.screen.words

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentWordsBinding
import com.example.yaroslavgorbach.voclevelup.nav

class WordsFragment : Fragment(R.layout.fragment_words) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<WordsViewModel>()
        val wordsView = WordsViewImp(
            FragmentWordsBinding.bind(view),
            nav::openWord, nav::openAddWord)
        vm.words.observe(viewLifecycleOwner, wordsView::setWords)
        vm.loading.observe(viewLifecycleOwner, wordsView::setLoading)
    }
}