package com.example.yaroslavgorbach.voclevelup.screen.word

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.data.Word
import java.io.IOException

class WordFragment : Fragment(R.layout.fragment_word) {

    companion object {
        private const val ARG_WORD_TEXT = "ARG_WORD_TEXT"
        fun argsOf(word: Word) = bundleOf(ARG_WORD_TEXT to word.text)
    }

    private val wordText by lazy { requireNotNull(arguments?.getString(ARG_WORD_TEXT)) }
    private val repo = RepoProvider.provideRepo()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.wordText).text = wordText
        initTranslation(view)
    }

    private fun initTranslation(view: View) {
        val transTv = view.findViewById<TextView>(R.id.wordTranslation)
        val loadTranslation = {
            transTv.isEnabled = false
            transTv.setText(R.string.loading_translation)
            lifecycleScope.launchWhenStarted {
                try {
                    transTv.text = repo.getTranslation(wordText)
                } catch (e: IOException) {
                    transTv.setText(R.string.cant_load_translation)
                }
                transTv.isEnabled = true
            }
        }
        transTv.setOnClickListener { loadTranslation() }
        loadTranslation()
    }
}