package com.example.yaroslavgorbach.voclevelup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

        // set soft input mode for different fragments
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                    if (f is AddWordFragment) {
                        window.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN)
                    }
                }

                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    if (f is AddWordFragment) {
                        window.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    }
                }
            }, false
        )
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

    override val onDeleteWord = MutableLiveEvent<String>()

    override fun onDeleteWord(text: String) {
        supportFragmentManager.popBackStack()
        onDeleteWord.send(text)
    }
}