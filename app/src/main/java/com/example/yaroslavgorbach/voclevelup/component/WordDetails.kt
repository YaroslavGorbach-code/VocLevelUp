package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.component.WordDetails.*
import com.example.yaroslavgorbach.voclevelup.data.Word
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow

interface WordDetails {
    val word: LiveData<String>
    val details: LiveData<State>

    sealed class State {
        object Loading : State()
        object Error : State()
        data class Data(val word: Word) : State()
    }
}

@ExperimentalCoroutinesApi
class WordDetailsImp(wordText: String, repo: Repo): WordDetails {

    override val word = MutableLiveData(wordText)
    override val details: LiveData<State> = flow {
        emit(State.Loading)
        val word = repo.getWord(wordText)
        if (word != null) {
            emit(State.Data(word))
        } else {
            emit(State.Error)
        }
    }.asLiveData()
}