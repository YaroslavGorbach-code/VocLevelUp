package com.example.yaroslavgorbach.voclevelup.workflow

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.databinding.WorkflowNavBinding
import com.example.yaroslavgorbach.voclevelup.feature.BaseFragment
import com.example.yaroslavgorbach.voclevelup.feature.awaitReady
import com.example.yaroslavgorbach.voclevelup.feature.explore.ExploreFragment
import com.example.yaroslavgorbach.voclevelup.feature.learn.LearnFragment
import com.example.yaroslavgorbach.voclevelup.feature.loadTransition
import com.example.yaroslavgorbach.voclevelup.util.host
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class NavWorkflow : BaseFragment(R.layout.workflow_nav), DictWorkflow.Router {


    interface Router {
        fun openAddWord(srcView: View)
    }

    override fun onViewReady(view: View, init: Boolean) {
        if (init) {
            val dictWorkflow = DictWorkflow()
            childFragmentManager.commit {
                add(R.id.nav_container, dictWorkflow)
                setPrimaryNavigationFragment(dictWorkflow)
                setReorderingAllowed(true)
            }
            postponeUntil {
                dictWorkflow.awaitReady()
            }
        }
        val bind = WorkflowNavBinding.bind(view)
        bind.navPager.apply {
            setOnNavigationItemSelectedListener {
                if (selectedItemId != it.itemId) {
                    // exit
                    val currentFragment = childFragmentManager.findFragmentById(R.id.nav_container)!!
                    currentFragment.exitTransition = loadTransition(R.transition.change_nav)
                    // enter
                    val newFragment = when (it.itemId) {
                        R.id.menu_nav_dict -> DictWorkflow()
                        R.id.menu_nav_learn -> LearnFragment()
                        R.id.menu_nav_explore -> ExploreFragment()
                        else -> error("Unknown menu: ${it.title}")
                    }
                    newFragment.enterTransition = loadTransition(R.transition.change_nav)
                    // transaction
                    childFragmentManager.commit {
                        replace(R.id.nav_container, newFragment)
                        setPrimaryNavigationFragment(newFragment)
                        setReorderingAllowed(true)
                    }
                } else {
                    childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                true
            }
        }
    }

    override fun openAddWord(srcView: View) = host<Router>().openAddWord(srcView)
}