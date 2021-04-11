package com.example.yaroslavgorbach.voclevelup.screen.addword

import androidx.core.widget.doAfterTextChanged
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature.State.*


interface AddWordView {
    fun setTranslation(state: TranslationFeature.State?)
    fun setSaveEnable(enabled: Boolean)

    interface Callback {
        fun onWordInput(text: String)
        fun onSaveClick()
    }
}

class AddWordImp(
    private val binding: FragmentAddWordBinding,
    private val callback: AddWordView.Callback
) : AddWordView {

    init {
        binding.addWordSave.setOnClickListener {callback.onSaveClick()}
        binding.addWordInput.doAfterTextChanged { callback.onWordInput(it.toString()) }
    }

    override fun setTranslation(state: TranslationFeature.State?) = with(binding) {
        addWordTranslation.isEnabled = state is Success
        when(state){
            null -> addWordTranslation.text = ""
            Progress -> addWordTranslation.setText(R.string.loading_translation)
            Fail -> addWordTranslation.setText(R.string.cant_load_translation)
            is Success -> addWordTranslation.text = state.result
        }
    }

    override fun setSaveEnable(enabled: Boolean) {
        binding.addWordSave.isEnabled = enabled
    }
}