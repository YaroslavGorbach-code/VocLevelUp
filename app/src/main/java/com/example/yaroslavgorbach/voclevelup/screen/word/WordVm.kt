package com.example.yaroslavgorbach.voclevelup.screen.word

import androidx.lifecycle.*
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart

interface WordVm{
    val word: LiveData<String>
    val translation: LiveData<TranslationFeature.State>
    fun onRetry()
}

@ExperimentalCoroutinesApi
class WordVmImp(wordText: String): ViewModel(), WordVm {

    companion object {
        fun createFactory(wordText: String) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return WordVmImp(wordText) as T
            }
        }
    }

    private val transFeature = TranslationFeature(repo)
    private val retryEvent = Channel<Unit>()

    override val word: LiveData<String> = MutableLiveData(wordText)
    override val translation: LiveData<TranslationFeature.State> =
        retryEvent
            .consumeAsFlow()
            .onStart { emit(Unit) }
            .flatMapLatest { transFeature.getTranslation(wordText) }
            .asLiveData()

    override fun onRetry() {
        retryEvent.offer(Unit)
    }
}