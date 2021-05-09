package com.example.yaroslavgorbach.voclevelup.screen.word

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.App
import com.example.yaroslavgorbach.voclevelup.di.DaggerWordComponent
import com.example.yaroslavgorbach.voclevelup.di.WordComponent
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class WordViewModel(app: Application) : AndroidViewModel(app) {

    private var wordComponent: WordComponent? = null

    fun getWordComponent(word: String): WordComponent {
        if (wordComponent == null) {
            wordComponent = DaggerWordComponent.factory()
                .create(this, word, getApplication<App>().appComponent)
        }
        return wordComponent!!
    }
}