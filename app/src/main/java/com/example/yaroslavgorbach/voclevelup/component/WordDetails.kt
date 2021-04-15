package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.component.WordDetails.*
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.asStateFlow
import com.example.yaroslavgorbach.voclevelup.util.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart

interface WordDetails {
    val text: LiveData<String>
    val translations: LiveData<List<String>?>
    val onWordNotFound: LiveEvent<Unit>
}

@InternalCoroutinesApi
class WordDetailsImp(wordText: String, repo: Repo, scope: CoroutineScope) : WordDetails {

    override val onWordNotFound = MutableLiveEvent<Unit>()

    private val word = flow {
        val word = repo.getWord(wordText)
        if (word == null) {
            onWordNotFound.send()
        }
        emit(word)
    }.asStateFlow(scope)

    override val text = word.mapNotNull { it?.text }.onStart { emit(wordText) }.asLiveData()
    override val translations = word.map { it?.translations }.onStart { emit(null) }.asLiveData()
}