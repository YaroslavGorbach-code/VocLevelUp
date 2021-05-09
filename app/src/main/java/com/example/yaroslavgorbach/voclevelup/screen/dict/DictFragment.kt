package com.example.yaroslavgorbach.voclevelup.screen.dict

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.App
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.Dictionary
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentDictBinding
import com.example.yaroslavgorbach.voclevelup.di.DaggerDictComponent
import com.example.yaroslavgorbach.voclevelup.screen.word.WordFragment
import com.example.yaroslavgorbach.voclevelup.util.consume
import com.example.yaroslavgorbach.voclevelup.util.router
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class DictFragment : Fragment(R.layout.fragment_dict),  WordFragment.Target {
    interface Router {
        fun openWord(text: String, target: Fragment)
        fun openAddWord()
    }

    @Inject lateinit var dictModel: Dictionary
    private lateinit var dictView: DictView

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        DaggerDictComponent.factory()
            .create(this, (requireActivity().application as App).appComponent)
            .inject(this)

        dictView = DictView(FragmentDictBinding.bind(requireView()), object : DictView.Callback {
            override fun onAdd() = router<Router>().openAddWord()
            override fun onSwipe(word: Word) = dictModel.onRemove(word)
            override fun onClick(word: Word) = router<Router>().openWord(word.text, this@DictFragment)
        })
        with(dictModel) {
            words.observe(viewLifecycleOwner, dictView::setWords)
            loading.observe(viewLifecycleOwner, dictView::setLoading)
            onWordRemoved.consume(viewLifecycleOwner) {
                dictView.showRemoveWordUndo { dictModel.restoreWord(it) }
            }
        }
    }


    override fun onWordDeleted(word: Word) {
        lifecycleScope.launchWhenStarted {
            dictView.showRemoveWordUndo { dictModel.restoreWord(word) }
        }
    }
}
