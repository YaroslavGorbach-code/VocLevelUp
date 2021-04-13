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

class MainActivity : AppCompatActivity(R.layout.activity_main), Navigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.mainContainer, NavFragment())
            }
        }
    }

    override fun openWord(word: Word) {
        supportFragmentManager.commit {
            replace(R.id.mainContainer, WordFragment::class.java, WordFragment.argsOf(word))
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            addToBackStack(null)
        }
    }

    override fun openAddWord() {
        supportFragmentManager.commit {
            replace(R.id.mainContainer, AddWordFragment())
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            addToBackStack(null)
        }
    }

    override fun up() = onBackPressed()

}