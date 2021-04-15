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
import com.example.yaroslavgorbach.voclevelup.util.consume
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class WordFragment : Fragment(R.layout.fragment_word) {

    interface Host {
        fun onDeleteWord(text: String)
    }

    companion object {
        fun argsOf(word: Word) = bundleOf("word" to word.text)
        private val WordFragment.wordText get() = requireArguments()["word"] as String
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<WordViewModel>()
        val wordDetails = vm.wordDetails(wordText)
        val v = WordView(FragmentWordBinding.bind(view), object : WordView.Callback {
            override fun onUp() = nav.up()
            override fun onDelete() = (activity as Host).onDeleteWord(wordText)
            override fun onMove(from: Int, to: Int) = wordDetails.onMoveTrans(from, to)
        })

        with(wordDetails) {
            translations.observe(viewLifecycleOwner, v::setTranslations)
            text.observe(viewLifecycleOwner, v::setWordText)
            onWordNotFound.consume(viewLifecycleOwner, nav::up)
        }
    }
}