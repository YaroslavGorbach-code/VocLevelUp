package com.example.yaroslavgorbach.voclevelup.screen.word

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.WordDetails
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentWordBinding
import com.example.yaroslavgorbach.voclevelup.nav
import com.example.yaroslavgorbach.voclevelup.util.consume
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class WordFragment : Fragment(R.layout.fragment_word), AddTransDialog.Host, EditTransDialog.Host{

    interface Host {
        fun onDeleteWord(text: String)
    }

    companion object {
        fun argsOf(word: String) = bundleOf("word" to word)
        private val WordFragment.wordText get() = requireArguments()["word"] as String
    }

    private val vm by viewModels<WordViewModel>()
    private val details by lazy { vm.wordDetails(wordText) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val v = WordView(FragmentWordBinding.bind(view), object : WordView.Callback {
            override fun onUp() = nav.up()
            override fun onDelete() = (activity as Host).onDeleteWord(wordText)
            override fun onDeleteTrans(trans: String) = details.onDeleteTrans(trans)
            override fun onListen() {
                Toast.makeText(
                    context, "You're listening ${details.text.value}", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onReorderTrans(newTrans: List<String>) = details.onReorderTrans(newTrans)
            override fun onAddTrans() = AddTransDialog().show(childFragmentManager, null)
            override fun onEditTrans(trans: String) {
                EditTransDialog().apply { arguments = EditTransDialog.argsOf(trans) }
                    .show(childFragmentManager, null)
            }
        })

        with(details) {
            translations.observe(viewLifecycleOwner, v::setTranslations)
            text.observe(viewLifecycleOwner, v::setWordText)
            onTransDeleted.consume(viewLifecycleOwner, v::showDeleteTransUndo)
        }
    }

    override fun onAddTrans(text: String) = details.onAddTrans(text)
    override fun onEditTrans(trans: String, newText: String) = details.onEditTrans(trans, newText)
    override fun onDeleteTrans(trans: String) = details.onDeleteTrans(trans)
}