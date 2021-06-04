package com.example.yaroslavgorbach.voclevelup

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.yaroslavgorbach.voclevelup.databinding.WorkflowNavBinding
import com.example.yaroslavgorbach.voclevelup.feature.BaseFragment
import com.example.yaroslavgorbach.voclevelup.feature.awaitReady
import com.example.yaroslavgorbach.voclevelup.feature.delayTransition
import com.example.yaroslavgorbach.voclevelup.feature.explore.ExploreFragment
import com.example.yaroslavgorbach.voclevelup.feature.learn.LearnFragment
import com.example.yaroslavgorbach.voclevelup.util.host
import com.example.yaroslavgorbach.voclevelup.workflow.DictWorkflow
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class NavWorkflow : BaseFragment(R.layout.workflow_nav), DictWorkflow.Router {

    interface Router {
        fun openAddWord(srcView: View)
    }

    override fun onViewReady(view: View, init: Boolean) {
        val currentFragment = if (init) {
            val dictFragment = DictWorkflow()
            childFragmentManager.commit {
                add(R.id.nav_container, dictFragment)
                setPrimaryNavigationFragment(dictFragment)
                setReorderingAllowed(true)
            }
            dictFragment
        } else {
            childFragmentManager.findFragmentById(R.id.nav_container) as BaseFragment
        }
        delayTransition { currentFragment.awaitReady() }

        val bind = WorkflowNavBinding.bind(view)
        bind.navPager.apply {
            setOnNavigationItemSelectedListener {
                if (selectedItemId != it.itemId) {
                    val fragment = when (it.itemId) {
                        R.id.menu_nav_dict -> DictWorkflow()
                        R.id.menu_nav_learn -> LearnFragment()
                        R.id.menu_nav_explore -> ExploreFragment()
                        else -> error("Unknown menu: ${it.title}")
                    }
                    childFragmentManager.commit {
                        replace(R.id.nav_container, fragment)
                        setPrimaryNavigationFragment(fragment)
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    }
                } else {
                    childFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                true
            }
        }
    }

    override fun openAddWord(srcView: View) = host<Router>().openAddWord(srcView)
}