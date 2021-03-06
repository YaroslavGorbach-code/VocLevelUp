package com.example.yaroslavgorbach.voclevelup.util

import androidx.fragment.app.Fragment

inline fun <reified T> Fragment.host(): T = (parentFragment ?: activity) as T

fun Fragment.forEachChildRecursive(action: (Fragment) -> Unit) {
    childFragmentManager.fragments.forEach {
        action(it)
        it.forEachChildRecursive(action)
    }
}

fun Fragment.forEachParentRecursive(action: (Fragment) -> Unit) {
    parentFragment?.let {
        action(it)
        it.forEachParentRecursive(action)
    }
}