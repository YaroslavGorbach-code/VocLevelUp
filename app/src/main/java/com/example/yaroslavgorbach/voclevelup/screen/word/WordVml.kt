package com.example.yaroslavgorbach.voclevelup.screen.word

import androidx.lifecycle.*
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

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
    private val loadTransEvent = MutableStateFlow(Any())

    override val word: LiveData<String> = MutableLiveData(wordText)
    override val translation: LiveData<TranslationFeature.State> =
        loadTransEvent
            .flatMapLatest { transFeature.getTranslation(wordText) }
            .asLiveData()

    override fun onRetry() {
        loadTransEvent.value = Any()
    }
}