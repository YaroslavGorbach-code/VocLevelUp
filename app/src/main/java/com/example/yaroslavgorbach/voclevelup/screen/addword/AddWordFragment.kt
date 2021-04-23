package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.screen.word.WordFragment
import com.example.yaroslavgorbach.voclevelup.util.router
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AddWordFragment : Fragment(R.layout.fragment_add_word), WordFragment.Target {

    interface Router {
        fun openWord(text: String, target: Fragment)
    }

    private val vm by viewModels<AddWordViewModel>()
    private val addWordModel by lazy { vm.addWord }
    private lateinit var addWordView: AddWordView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addWordView = AddWordView(FragmentAddWordBinding.bind(view), object : AddWordView.Callback {
            override fun onOpen(item: AddWord.DefItem) = router<Router>().openWord(item.text, this@AddWordFragment)
            override fun onSave(item: AddWord.DefItem) = addWordModel.onSave(item)
            override fun onInput(input: String) = addWordModel.onInput(input)
            override fun onLangClick(lang: Language) = addWordModel.onChooseLang(lang)
            override fun onRetry() = addWordModel.onRetry()
        })
        with(addWordModel) {
            addWordView.setMaxWordLength(maxWordLength)
            definitions.observe(viewLifecycleOwner) { addWordView.setDefState(it) }
            languages.observe(viewLifecycleOwner) { addWordView.setLanguages(it) }
        }
    }

    override fun onWordDeleted(word: Word) {
        lifecycleScope.launchWhenStarted {
            addWordView.showUndoDeleteWord { addWordModel.onRestoreWord(word) }
        }
    }
}