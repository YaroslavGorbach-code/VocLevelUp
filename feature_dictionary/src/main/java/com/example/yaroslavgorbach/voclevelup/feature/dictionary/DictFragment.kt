package com.example.yaroslavgorbach.voclevelup.feature.dictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.data.api.Word
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.databinding.FragmentDictBinding
import com.example.yaroslavgorbach.voclevelup.util.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class DictFragment : Fragment(R.layout.fragment_dict){
    interface Router {
        fun openWord(text: String, target: Fragment)
        fun openAddWord()
    }

    private val vm by viewModels<DictViewModel>()
    @Inject lateinit var dictModel: Dictionary

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        vm.dictComponent.inject(this)

        val v = DictView(FragmentDictBinding.bind(requireView()), object : DictView.Callback {
            override fun onAdd() = host<Router>().openAddWord()
            override fun onSwipe(word: Word) = dictModel.onRemove(word)
            override fun onClick(word: Word) = host<Router>().openWord(word.text, this@DictFragment)
        })
        with(dictModel) {
            words.observe(viewLifecycleOwner, v::setWords)
            loading.observe(viewLifecycleOwner, v::setLoading)
            onWordRemoved.consume(viewLifecycleOwner) {
                v.showRemoveWordUndo { lifecycleScope.launch { it() } }
            }
        }
    }
}
