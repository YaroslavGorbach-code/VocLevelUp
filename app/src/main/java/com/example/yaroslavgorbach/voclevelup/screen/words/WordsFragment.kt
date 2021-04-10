package com.example.yaroslavgorbach.voclevelup.screen.words

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.nav

class WordsFragment : Fragment(R.layout.fragment_words) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm = ViewModelProvider(this)[WordsViewModel::class.java]
        val wordsView = WordsViewImp(view, nav::openWord, nav::openAddWord)
        vm.words.observe(viewLifecycleOwner, wordsView::setWords)
        vm.loading.observe(viewLifecycleOwner, wordsView::setLoading)
    }
}