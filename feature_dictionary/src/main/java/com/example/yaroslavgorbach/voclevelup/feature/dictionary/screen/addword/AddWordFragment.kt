package com.example.yaroslavgorbach.voclevelup.feature.dictionary.screen.addword

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.R
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.component.AddWord
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.screen.word.WordFragment
import com.example.yaroslavgorbach.voclevelup.feature.router
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class AddWordFragment : Fragment(R.layout.fragment_add_word){

    interface Router {
        fun openWord(text: String)
    }

    @Inject lateinit var addWordModel: AddWord

    private val vm by viewModels<AddWordViewModel>()

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        vm.addWordComponent.inject(this)
        val v =
            AddWordView(FragmentAddWordBinding.bind(requireView()), object : AddWordView.Callback {
                override fun onOpen(item: AddWord.DefItem) = router<Router>().openWord(item.text)
                override fun onSave(item: AddWord.DefItem) = addWordModel.onSave(item)
                override fun onInput(input: String) = addWordModel.onInput(input)
                override fun onLangClick(lang: Language) = addWordModel.onChooseLang(lang)
                override fun onRetry() = addWordModel.onRetry()
                override fun onInputDone(text: String) = addWordModel.onSearch(text)
                override fun onCompClick(text: String) = addWordModel.onSearch(text)
            })

        with(addWordModel) {
            v.setMaxWordLength(maxWordLength)
            languages.observe(viewLifecycleOwner, v::setLanguages)
            state.observe(viewLifecycleOwner, v::setState)
        }
    }
}