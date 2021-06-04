package com.example.yaroslavgorbach.voclevelup.feature

import android.content.Context
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.ViewGroup
import androidx.annotation.TransitionRes
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun BaseFragment.awaitReady() {
    suspendCancellableCoroutine<Unit> {
        doOnReadyForTransition {
            it.resume(Unit)
        }
    }
}

suspend fun LiveData<*>.awaitValue() {
    asFlow().first()
}

fun Fragment.loadTransition(@TransitionRes transRes: Int): Transition =
    requireContext().loadTransition(transRes)

fun Context.loadTransition(@TransitionRes transRes: Int): Transition =
    TransitionInflater.from(this).inflateTransition(transRes)