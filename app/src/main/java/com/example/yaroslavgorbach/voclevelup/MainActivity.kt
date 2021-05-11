package com.example.yaroslavgorbach.voclevelup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.*
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.worddetails.WordFragment
import com.example.yaroslavgorbach.voclevelup.workflow.NavWorkflow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity(R.layout.activity_main), NavWorkflow.Router {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.main_container, NavWorkflow().also { setPrimaryNavigationFragment(it) })
            }
        }

        // configure input mode
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    window.setSoftInputMode(
                        when (f) {
                            is WordFragment -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                            is DialogFragment -> window.attributes.softInputMode
                            else -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        }
                    )
                }
            },
            true
        )

    }

    override fun openAddWord() {
        supportFragmentManager.commit {
            supportFragmentManager.findFragmentById(R.id.main_container)?.let(::hide)
            replace(R.id.main_container, com.example.yaroslavgorbach.voclevelup.feature.dictionary.AddWordWorkflow()
                .also { setPrimaryNavigationFragment(it) })
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            addToBackStack(null)
        }
    }
}