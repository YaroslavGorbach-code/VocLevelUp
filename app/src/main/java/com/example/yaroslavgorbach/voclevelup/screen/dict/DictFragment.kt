package com.example.yaroslavgorbach.voclevelup.screen.dict

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentDictBinding
import com.example.yaroslavgorbach.voclevelup.nav
import com.example.yaroslavgorbach.voclevelup.util.consume

class DictFragment : Fragment(R.layout.fragment_dict) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<DictViewModel>()
        val v = DictView(FragmentDictBinding.bind(view), object : DictView.Callback {
            override fun onAdd() = nav.openAddWord()
            override fun onSwipe(word: Word) = vm.dictionary.onRemove(word)
            override fun onClick(word: Word) = nav.openWord(word)
        })

        with(vm.dictionary) {
            words.observe(viewLifecycleOwner, v::setWords)
            loading.observe(viewLifecycleOwner, v::setLoading)
            onUndoRemoved.consume(viewLifecycleOwner, consumer = v::showRemoveWordUndo)
        }
        nav.onDeleteWord.consume(viewLifecycleOwner, vm.dictionary::onRemove)
    }
}
