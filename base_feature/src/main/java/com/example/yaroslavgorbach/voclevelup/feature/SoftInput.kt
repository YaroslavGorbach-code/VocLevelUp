package com.example.yaroslavgorbach.voclevelup.feature

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface SoftInput {
    fun show(view: View)
    fun hide()
    var nextShowDelay: Long
}

interface SoftInputProvider {
    val softInput: SoftInput
}

val View.softInput: SoftInput get() = (context as SoftInputProvider).softInput
