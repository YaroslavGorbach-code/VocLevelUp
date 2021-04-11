package com.example.yaroslavgorbach.voclevelup.screen.word

import androidx.lifecycle.ViewModel
import com.example.yaroslavgorbach.voclevelup.component.WordDetails
import com.example.yaroslavgorbach.voclevelup.component.WordDetailsImp
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class WordViewModel : ViewModel() {

    private var wordDetails: WordDetails? = null

    fun wordDetails(wordText: String): WordDetails {
        return wordDetails ?: WordDetailsImp(wordText, repo).also { wordDetails = it }
    }
}