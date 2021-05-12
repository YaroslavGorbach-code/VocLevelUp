package com.example.yaroslavgorbach.voclevelup.feature.dictionary.screen.word

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.di.DaggerWordComponent
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.di.WordComponent
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class WordViewModel(private val app: Application) : AndroidViewModel(app) {

    private var wordComponent: WordComponent? = null

    fun getWordComponent(word: String): WordComponent {
        return wordComponent ?: DaggerWordComponent.factory().create(this, word, app as RepoProvider)
            .also { wordComponent = it }
    }
}