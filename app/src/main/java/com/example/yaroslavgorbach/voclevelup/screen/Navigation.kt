package com.example.yaroslavgorbach.voclevelup.screen

import androidx.fragment.app.Fragment
import com.example.yaroslavgorbach.voclevelup.data.Word

interface Navigation {
    fun openWord(word: Word)
    fun openAddWord()
}

val Fragment.nav get() = activity as Navigation
