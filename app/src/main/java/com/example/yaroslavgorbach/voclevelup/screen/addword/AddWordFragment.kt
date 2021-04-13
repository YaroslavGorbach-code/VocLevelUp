package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.nav
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

class AddWordFragment : Fragment(R.layout.fragment_add_word) {

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<AddWordViewModel>()
        val v = AddWordView(FragmentAddWordBinding.bind(view), object : AddWordView.Callback {
            override fun onSave(item: AddWord.TransItem) = vm.addWord.onSave(item)
            override fun onRemove(item: AddWord.TransItem) = vm.addWord.onRemove(item)
            override fun onInput(input: String) = vm.addWord.onInput(input)
            override fun onLangClick(lang: Language) = vm.addWord.onChooseLang(lang)
            override fun onUp() = nav.up()
        })

        with(vm.addWord) {
            translation.observe(viewLifecycleOwner, v::setTranslation)
            v.setMaxWordLength(maxWordLength)
            languages.observe(viewLifecycleOwner, v::setLanguages)
        }
    }
}