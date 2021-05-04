package com.example.yaroslavgorbach.voclevelup.screen.word

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.WordDetails
import com.example.yaroslavgorbach.voclevelup.component.WordDetailsImp
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class WordViewModel(app: Application) : AndroidViewModel(app) {

    private var wordDetails: WordDetails? = null

    fun wordDetails(wordText: String): WordDetails {
        return wordDetails ?: WordDetailsImp(wordText, repo, viewModelScope).also {
            wordDetails = it
        }
    }
}