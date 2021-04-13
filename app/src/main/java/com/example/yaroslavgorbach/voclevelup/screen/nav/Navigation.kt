package com.example.yaroslavgorbach.voclevelup.screen.nav
import com.example.yaroslavgorbach.voclevelup.data.Word

interface Navigation {
    fun openWord(word: Word)
    fun openAddWord()
    fun up()
}
