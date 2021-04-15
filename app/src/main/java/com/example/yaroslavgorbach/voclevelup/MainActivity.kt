package com.example.yaroslavgorbach.voclevelup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.screen.nav.Navigation
import com.example.yaroslavgorbach.voclevelup.screen.addword.AddWordFragment
import com.example.yaroslavgorbach.voclevelup.screen.nav.NavFragment
import com.example.yaroslavgorbach.voclevelup.screen.word.WordFragment
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.send
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity(R.layout.activity_main), Navigation, WordFragment.Host {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.main_container, NavFragment())
            }
        }
    }

    override fun openWord(word: Word) {
        supportFragmentManager.commit {
            supportFragmentManager.findFragmentById(R.id.main_container)?.let(::hide)
            add(R.id.main_container, WordFragment::class.java, WordFragment.argsOf(word))
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            addToBackStack(null)
        }
    }

    override fun openAddWord() {
        supportFragmentManager.commit {
            replace(R.id.main_container, AddWordFragment())
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            addToBackStack(null)
        }
    }

    override fun up() = onBackPressed()

    override val onDeleteWord = MutableLiveEvent<Word>()

    override fun onDeleteWord(word: Word) {
        supportFragmentManager.popBackStack()
        onDeleteWord.send(word)
    }
}