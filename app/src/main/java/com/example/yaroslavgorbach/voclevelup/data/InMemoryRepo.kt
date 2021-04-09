package com.example.yaroslavgorbach.voclevelup.data

import androidx.lifecycle.MutableLiveData

object InMemoryRepo : Repo {

    private val words = MutableLiveData<List<Word>>(listOf(
        Word("Hello"),
        Word("Word")
    ))
    override fun getAllWords() = words

}