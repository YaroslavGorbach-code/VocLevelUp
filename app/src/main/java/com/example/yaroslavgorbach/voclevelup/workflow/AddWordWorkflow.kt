package com.example.yaroslavgorbach.voclevelup.workflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.feature.BaseFragment
import com.example.yaroslavgorbach.voclevelup.feature.addword.AddWordFragment
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.WordFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@InternalCoroutinesApi
class AddWordWorkflow : BaseFragment(R.layout.workflow_add_word), AddWordFragment.Router, WordFragment.Router {

    override fun onViewReady(view: View, init: Boolean) {
        if (init) {
            childFragmentManager.commitNow {
                add(R.id.add_word_container, AddWordFragment())
            }
        }
    }

    override fun openWord(text: String) {
        childFragmentManager.commit {
            replace(R.id.add_word_container, WordFragment::class.java, WordFragment.argsOf(text))
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
    }

    override fun onWordDeleted(undo: suspend () -> Unit) {
        childFragmentManager.popBackStack()
        Snackbar.make(requireView(), R.string.word_removed, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) { lifecycleScope.launch { undo() } }
            .show()
    }
}