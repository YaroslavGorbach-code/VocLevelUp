package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart

interface WordDetails {
    val word: LiveData<String>
    val translation: LiveData<Translation.State>
    fun onRetry()
}

@ExperimentalCoroutinesApi
class WordDetailsImp(wordText: String, repo: Repo) :
    WordDetails {

    private val transFeature = Translation(repo)
    private val retryEvent = Channel<Unit>()

    override val word: LiveData<String> = MutableLiveData(wordText)
    override val translation: LiveData<Translation.State> =
        retryEvent.consumeAsFlow()
            .onStart { emit(Unit) }
            .flatMapLatest { transFeature.getTranslation(wordText) }
            .asLiveData()

    override fun onRetry() {
        retryEvent.offer(Unit)
    }
}