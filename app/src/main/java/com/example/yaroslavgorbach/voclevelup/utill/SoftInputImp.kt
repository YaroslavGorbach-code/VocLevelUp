package com.example.yaroslavgorbach.voclevelup.utill

import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.feature.SoftInput
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.WordFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoftInputImp(private val activity: FragmentActivity) : SoftInput {

    private val imm = activity.getSystemService<InputMethodManager>()

    override var nextShowDelay = 0L

    fun configureInputMode() {
        // configure input mode (need custom settings in some fragments for better ui)
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    when (f) {
                        is WordFragment -> {
                            activity.window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
                        }
                        !is DialogFragment -> {
                            activity.window.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
                        }
                    }
                }
            },
            true
        )
    }

    override fun show(view: View) {
        activity.lifecycleScope.launch {
            delay(nextShowDelay)
            view.clearFocus() // weird bug, without resetting focus keyboard not opens next time
            view.requestFocus()
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            nextShowDelay = 0L
        }
    }

    override fun hide() {
        activity.currentFocus?.windowToken?.let {
            imm?.hideSoftInputFromWindow(it, 0)
        }
    }
}