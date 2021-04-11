package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.Word
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface WordList{
    val words: LiveData<List<Word>>
    val loading: LiveData<Boolean>
}

class WordListImp(repo: Repo): WordList {
    override val words: LiveData<List<Word>> = repo.getAllWords().asLiveData()
    override val loading: LiveData<Boolean> =
        repo.getAllWords()
            .map { it as List<Word>? }
            .onStart { emit(null) }
            .map { it == null }
            .asLiveData()
}