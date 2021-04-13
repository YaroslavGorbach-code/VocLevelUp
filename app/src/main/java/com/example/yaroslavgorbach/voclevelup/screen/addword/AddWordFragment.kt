package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.component.AddWord.Translation
import com.example.yaroslavgorbach.voclevelup.data.Definition
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.util.consume
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class AddWordFragment : Fragment(R.layout.fragment_add_word) {
    interface Host {
        fun onWordAdded(text: String)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<AddWordViewModel>()
        val v = AddWordView(FragmentAddWordBinding.bind(view), object : AddWordView.Callback {
            override fun onSave() = vm.addWord.onSave()
            override fun onInput(input: String) = vm.addWord.onInput(input)
            override fun onLangClick(lang: Language) = vm.addWord.onChooseLang(lang)
        })

        with(vm.addWord) {
            onWordAdded.consume(viewLifecycleOwner, (activity as Host)::onWordAdded)
            translation.observe(viewLifecycleOwner, v::setTranslation)
            saveEnabled.observe(viewLifecycleOwner, v::setSaveEnabled)
            v.setMaxWordLength(maxWordLength)
            languages.observe(viewLifecycleOwner, v::setLanguages)
        }
    }
}