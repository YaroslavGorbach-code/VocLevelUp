package com.example.yaroslavgorbach.voclevelup.screen.word

import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentWordBinding
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature.State.*
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature.*


interface WordView {
    fun setWordText(text: String)
    fun setTranslation(state: State)
}

class WordViewImp(
    private val binding: FragmentWordBinding,
    private val onRetryClick: () -> Unit
) : WordView {

    init {
        binding.wordTranslation.setOnClickListener { onRetryClick() }
    }

    override fun setWordText(text: String) {
        binding.wordText.text = text
    }

    override fun setTranslation(state: State) = with(binding) {
        wordTranslation.isEnabled = state is Fail
        when (state) {
            is Success -> wordTranslation.text = state.result
            Fail -> wordTranslation.setText(R.string.reload_translation)
            Progress -> wordTranslation.setText(R.string.loading_translation)
        }
    }
}