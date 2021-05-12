package com.example.yaroslavgorbach.voclevelup.feature.dictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.screen.addword.AddWordFragment
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.screen.word.WordFragment
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AddWordWorkflow : Fragment(R.layout.workflow_add_word), AddWordFragment.Router {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                add(R.id.add_word_container, AddWordFragment())
            }
        }
    }

    override fun openWord(text: String, target: Fragment) {
        childFragmentManager.commit {
            replace(R.id.add_word_container, WordFragment().apply {
                arguments = WordFragment.argsOf(text)
                setTargetFragment(target, 0)
            })
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
    }
}