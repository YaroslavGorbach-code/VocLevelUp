package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.core.data.Language
import com.example.yaroslavgorbach.voclevelup.core.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.screen.word.WordFragment
import com.example.yaroslavgorbach.voclevelup.util.router
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class AddWordFragment : Fragment(R.layout.fragment_add_word), WordFragment.Target {

    interface Router {
        fun openWord(text: String, target: Fragment)
    }

    @Inject lateinit var addWordModel: AddWord
    private lateinit var addWordView: AddWordView
    private val vm by viewModels<AddWordViewModel>()

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        vm.addWordComponent.inject(this)
        addWordView = AddWordView(FragmentAddWordBinding.bind(requireView()), object : AddWordView.Callback {
            override fun onOpen(item: AddWord.DefItem) = router<Router>().openWord(item.text, this@AddWordFragment)
            override fun onSave(item: AddWord.DefItem) = addWordModel.onSave(item)
            override fun onInput(input: String) = addWordModel.onInput(input)
            override fun onLangClick(lang: Language) = addWordModel.onChooseLang(lang)
            override fun onRetry() = addWordModel.onRetry()
            override fun onInputDone(text: String) = addWordModel.onSearch(text)
            override fun onCompClick(text: String) = addWordModel.onSearch(text)
        })

        with(addWordModel) {
            addWordView.setMaxWordLength(maxWordLength)
            languages.observe(viewLifecycleOwner, addWordView::setLanguages)
            state.observe(viewLifecycleOwner, addWordView::setState)
        }
    }

    override fun onWordDeleted(word: Word) {
        lifecycleScope.launchWhenStarted {
            addWordView.showUndoDeleteWord { addWordModel.onRestoreWord(word) }
        }
    }
}