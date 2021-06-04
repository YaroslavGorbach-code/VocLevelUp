package com.example.yaroslavgorbach.voclevelup.workflow

import android.view.View
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.feature.BaseFragment
import com.example.yaroslavgorbach.voclevelup.feature.awaitReady
import com.example.yaroslavgorbach.voclevelup.feature.delayTransition
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.DictFragment
import com.example.yaroslavgorbach.voclevelup.feature.loadTransition
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.WordFragment
import com.example.yaroslavgorbach.voclevelup.util.host
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DictWorkflow : BaseFragment(R.layout.workflow_dict), DictFragment.Router,
    WordFragment.Router {

    interface Router {
        fun openAddWord(srcView: View)
    }

    override fun onViewReady(view: View, init: Boolean) {
        val dictFragment = if (init) {
            val fragment = DictFragment()
            childFragmentManager.commit {
                add(R.id.dict_container, fragment)
                setReorderingAllowed(true)
            }
            fragment
        } else {
            childFragmentManager.findFragmentById(R.id.dict_container) as DictFragment
        }
        delayTransition { dictFragment.awaitReady() }
    }

    override fun openWord(text: String, srcView: View) {
        // setup exit
        val dictFrag = childFragmentManager.findFragmentById(R.id.dict_container) as DictFragment
        dictFrag.exitTransition = loadTransition(R.transition.open_word_exit)
        // setup enter
        val wordFrag = WordFragment().apply { arguments = WordFragment.argsOf(text) }
        wordFrag.sharedElementEnterTransition = loadTransition(R.transition.open_word_enter_shared)
        // run transaction
        childFragmentManager.commit {
            replace(R.id.dict_container, wordFrag)
            addSharedElement(srcView, "word_root")
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onWordDeleted(undo: suspend () -> Unit) {
        childFragmentManager.popBackStack()
        Snackbar.make(requireView(), R.string.word_removed, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) { lifecycleScope.launch { undo() } }
            .show()
    }

    override fun openAddWord(srcView: View) = host<Router>().openAddWord(srcView)
}