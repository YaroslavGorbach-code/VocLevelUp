package com.example.yaroslavgorbach.voclevelup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import androidx.fragment.app.*
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.WordFragment
import com.example.yaroslavgorbach.voclevelup.workflow.AddWordWorkflow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity(R.layout.activity_main), NavFragment.Router {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.main_container, NavFragment().also { setPrimaryNavigationFragment(it) })
            }
        }

        // configure input mode (need custom settings in some fragments for better ui)
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    when (f) {
                        is WordFragment -> window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
                        !is DialogFragment -> {
                            @Suppress("DEPRECATION")
                            window.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
                        }
                    }
                }

            },
            true
        )
    }

    override fun openAddWord() {
        supportFragmentManager.commit {
            replace(R.id.main_container, AddWordWorkflow().also { setPrimaryNavigationFragment(it) })
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            addToBackStack(null)
        }
    }
}