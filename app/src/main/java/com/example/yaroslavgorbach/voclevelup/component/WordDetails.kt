package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

interface WordDetails {
    val word: LiveData<String>
    val details: LiveData<String?>
}

@ExperimentalCoroutinesApi
class WordDetailsImp(wordText: String, repo: Repo): WordDetails {

    override val word: LiveData<String> = MutableLiveData(wordText)

    override val details: LiveData<String?> = flow {
        emit(null)
        emit(repo.getDetails(wordText))
    }.asLiveData()
}