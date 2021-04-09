package com.example.yaroslavgorbach.voclevelup.data

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

interface Repo {
    fun getAllWords(): LiveData<List<Word>>
    suspend fun getTranslation(text: String): String
    suspend fun addWord(text: String)

}

val Fragment.repo: Repo get() = InMemoryRepo


data class Word(val text: String)