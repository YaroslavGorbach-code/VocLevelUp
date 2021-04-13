package com.example.yaroslavgorbach.voclevelup.screen
import com.example.yaroslavgorbach.voclevelup.data.Word

interface Navigation {
    fun openWord(word: Word)
    fun openAddWord()
    fun up()
}
