package com.example.yaroslavgorbach.voclevelup.screen.nav

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentNavBinding
import com.example.yaroslavgorbach.voclevelup.screen.explore.ExploreFragment
import com.example.yaroslavgorbach.voclevelup.screen.dict.DictFragment
import com.example.yaroslavgorbach.voclevelup.screen.learn.LearnFragment

class NavFragment : Fragment(R.layout.fragment_nav) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binder = FragmentNavBinding.bind(view)
        with(binder.navPager) {
            setOnNavigationItemSelectedListener {
                if (selectedItemId != it.itemId || childFragmentManager.fragments.isEmpty()) {
                    childFragmentManager.commit {
                        replace(
                            R.id.nav_container, when (it.itemId) {
                            R.id.menu_nav_dict -> DictFragment()
                            R.id.menu_nav_learn -> LearnFragment()
                            R.id.menu_nav_explore -> ExploreFragment()
                            else -> error("Unknown menu: ${it.title}")
                            }
                        )
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    }
                }
                true
            }
            selectedItemId = R.id.menu_nav_dict
        }
    }
}