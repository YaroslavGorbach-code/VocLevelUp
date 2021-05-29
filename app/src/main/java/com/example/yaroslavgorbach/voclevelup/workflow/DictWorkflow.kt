package com.example.yaroslavgorbach.voclevelup.workflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.DictFragment
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.WordFragment
import com.example.yaroslavgorbach.voclevelup.util.host
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class DictWorkflow : Fragment(R.layout.workflow_dict), DictFragment.Router, WordFragment.Router {

    interface Router {
        fun openAddWord()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                add(R.id.dict_container, DictFragment())
            }
        }
    }

    override fun openWord(text: String, target: Fragment) {
        childFragmentManager.commit {
            replace(R.id.dict_container, WordFragment::class.java, WordFragment.argsOf(text))
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            addToBackStack(null)
        }
    }

    override fun openAddWord() = host<Router>().openAddWord()

    override fun onWordDeleted(undo: suspend () -> Unit) {
        childFragmentManager.popBackStack()
        Snackbar.make(requireView(), R.string.word_removed, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) { lifecycleScope.launch { undo() } }
            .show()
    }
}