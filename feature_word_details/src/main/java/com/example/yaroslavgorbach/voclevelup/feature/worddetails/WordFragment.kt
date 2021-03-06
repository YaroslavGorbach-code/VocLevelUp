package com.example.yaroslavgorbach.voclevelup.feature.worddetails

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.yaroslavgorbach.voclevelup.feature.BaseFragment
import com.example.yaroslavgorbach.voclevelup.feature.awaitValue
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.databinding.FragmentWordBinding
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.di.WordViewModel
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.model.WordDetails
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.view.AddTransDialog
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.view.EditTransDialog
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.view.WordView
import com.example.yaroslavgorbach.voclevelup.util.consume
import com.example.yaroslavgorbach.voclevelup.util.host
import javax.inject.Inject

class WordFragment : BaseFragment(R.layout.fragment_word), AddTransDialog.Host,
    EditTransDialog.Host {

    interface Router {
        fun onWordDeleted(undo: suspend () -> Unit)
    }

    companion object {
        fun argsOf(word: String) = bundleOf("word" to word)
        private val WordFragment.word get() = requireArguments()["word"] as String
    }

    private val vm by viewModels<WordViewModel>()

    @Inject
    internal lateinit var detailsModel: WordDetails

    override fun onViewReady(view: View, init: Boolean) {
        vm.getWordComponent(word).inject(this)
        val v = WordView(FragmentWordBinding.bind(view), object : WordView.Callback {

            override fun onDelete() = detailsModel.onDeleteWord()
            override fun onAddTrans() = AddTransDialog().show(childFragmentManager, null)
            override fun onDeleteTrans(trans: String) = detailsModel.onDeleteTrans(trans)
            override fun onReorderTrans(newTrans: List<String>) =
                detailsModel.onReorderTrans(newTrans)

            override fun onListen() {
                Toast.makeText(
                    context,
                    "You're listening ${detailsModel.text.value}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onEditTrans(trans: String) {
                EditTransDialog().apply { arguments = EditTransDialog.argsOf(trans) }
                    .show(childFragmentManager, null)
            }
        })
        with(detailsModel) {
            translations.observe(viewLifecycleOwner, v::setTranslations)
            text.observe(viewLifecycleOwner, v::setWordText)
            pron.observe(viewLifecycleOwner, v::setPronText)
            onTransDeleted.consume(viewLifecycleOwner) {
                v.showDeleteTransUndo { lifecycleScope.launchWhenStarted { it() } }
            }
            onWordDeleted.consume(viewLifecycleOwner) {
                host<Router>().onWordDeleted(it)
            }
        }
        postponeUntil {
            detailsModel.translations.awaitValue()
            detailsModel.text.awaitValue()
            detailsModel.pron.awaitValue()
        }
    }

    override fun onAddTrans(text: String) = detailsModel.onAddTrans(text)
    override fun onEditTrans(trans: String, newText: String) =
        detailsModel.onEditTrans(trans, newText)

    override fun onDeleteTrans(trans: String) = detailsModel.onDeleteTrans(trans)
}