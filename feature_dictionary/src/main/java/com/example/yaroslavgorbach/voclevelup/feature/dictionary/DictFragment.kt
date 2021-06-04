package com.example.yaroslavgorbach.voclevelup.feature.dictionary

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.data.api.Word
import com.example.yaroslavgorbach.voclevelup.feature.BaseFragment
import com.example.yaroslavgorbach.voclevelup.feature.awaitValue
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.databinding.FragmentDictBinding
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.di.DictViewModel
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.model.Dictionary
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.view.DictView
import com.example.yaroslavgorbach.voclevelup.util.consume
import com.example.yaroslavgorbach.voclevelup.util.host
import kotlinx.coroutines.launch
import javax.inject.Inject

class DictFragment : BaseFragment(R.layout.fragment_dict) {

    interface Router {
        fun openWord(text: String, srcView: View)
        fun openAddWord(srcView: View)
    }

    private val vm by viewModels<DictViewModel>()

    @Inject
    internal lateinit var dictModel: Dictionary

    override fun onViewReady(view: View, init: Boolean) {
        vm.dictComponent.inject(this)
        val v = DictView(FragmentDictBinding.bind(view), object : DictView.Callback {
            override fun onAdd(srcView: View) = host<Router>().openAddWord(srcView)
            override fun onSwipe(word: Word) = dictModel.onRemove(word)
            override fun onClick(word: Word, srcView: View) =
                host<Router>().openWord(word.text, srcView)
        })
        with(dictModel) {
            words.observe(viewLifecycleOwner, v::setWords)
            loading.observe(viewLifecycleOwner, v::setLoading)
            onWordRemoved.consume(viewLifecycleOwner) {
                v.showRemoveWordUndo { lifecycleScope.launch { it() } }
            }
        }
        postponeUntil {
            dictModel.words.awaitValue()
        }
    }

    override fun getViewsSavedForTransition() = intArrayOf(R.id.dict_list)
}