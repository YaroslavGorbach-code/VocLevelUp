package com.example.yaroslavgorbach.voclevelup.screen.nav
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent

interface Navigation {
    val onDeleteWord: LiveEvent<String>
    fun openWord(word: Word)
    fun openAddWord()
    fun up()
}
