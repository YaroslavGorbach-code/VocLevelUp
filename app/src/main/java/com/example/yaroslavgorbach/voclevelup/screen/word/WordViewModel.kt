package com.example.yaroslavgorbach.voclevelup.screen.word

import androidx.lifecycle.*
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest

@ExperimentalCoroutinesApi
class WordViewModel(wordText: String): ViewModel() {

    companion object {
        fun createFactory(wordText: String) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return WordViewModel(wordText) as T
            }
        }
    }

    private val transFeature = TranslationFeature(repo)
    private val transEvent = MutableStateFlow(Any())

    val word: LiveData<String> = MutableLiveData(wordText)
    val translation: LiveData<TranslationFeature.State> =
        transEvent
            .flatMapLatest { transFeature.getTranslation(wordText) }
            .asLiveData()

    fun onRetry() {
        transEvent.value = Any()
    }
}