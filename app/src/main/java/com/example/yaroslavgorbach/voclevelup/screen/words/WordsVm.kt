package com.example.yaroslavgorbach.voclevelup.screen.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface WordsVm{
    val words: LiveData<List<Word>>
    val loading: LiveData<Boolean>
}

class WordsViewModel: ViewModel(), WordsVm {
    override val words: LiveData<List<Word>> = repo.getAllWords().asLiveData()
    override val loading: LiveData<Boolean> =
        repo.getAllWords()
            .map { it as List<Word>? }
            .onStart { emit(null) }
            .map { it == null }
            .asLiveData()
}