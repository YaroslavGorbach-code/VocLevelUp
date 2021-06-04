package com.example.yaroslavgorbach.voclevelup.feature.dictionary

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.data.api.Word
import com.example.yaroslavgorbach.voclevelup.feature.delayTransition
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.databinding.FragmentDictBinding
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.di.DictViewModel
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.model.Dictionary
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.view.DictView
import com.example.yaroslavgorbach.voclevelup.util.consume
import com.example.yaroslavgorbach.voclevelup.util.host
import kotlinx.coroutines.launch
import javax.inject.Inject

class DictFragment : Fragment(R.layout.fragment_dict) {

    interface Router {
        fun openWord(text: String, srcItem: View)
        fun openAddWord()
    }

    private val vm by viewModels<DictViewModel>()

    @Inject internal lateinit var dictModel: Dictionary

    private val viewState = SparseArray<Parcelable>()

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        vm.dictComponent.inject(this)
        val v = DictView(FragmentDictBinding.bind(requireView()), object : DictView.Callback {
            override fun onAdd() = host<Router>().openAddWord()
            override fun onSwipe(word: Word) = dictModel.onRemove(word)
            override fun onClick(word: Word, srcItem: View) = host<Router>().openWord(word.text, srcItem)
        })
        with(dictModel) {
            words.observe(viewLifecycleOwner, v::setWords)
            loading.observe(viewLifecycleOwner, v::setLoading)
            onWordRemoved.consume(viewLifecycleOwner) {
                v.showRemoveWordUndo { lifecycleScope.launch { it() } }
            }
        }
        delayTransition(dictModel.words) { view?.restoreHierarchyState(viewState) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.saveHierarchyState(viewState)
    }
}