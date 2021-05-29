package com.example.yaroslavgorbach.voclevelup.feature.dictionary.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.api.Repo
import com.example.yaroslavgorbach.voclevelup.data.api.Word
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class DictionaryImp(
    private val repo: Repo,
    private val scope: CoroutineScope
) : Dictionary {

    override val onWordRemoved = MutableLiveEvent<suspend () -> Unit>()
    override val words: LiveData<List<Word>> = repo.getAllWords().asLiveData(Dispatchers.IO)
    override val loading: LiveData<Boolean> =
        repo.getAllWords()
            .map { it as List<Word>? }
            .onStart { emit(null) }
            .map { it == null }
            .asLiveData(Dispatchers.IO)

    override fun onRemove(word: Word) {
        scope.launch {
            repo.deleteWord(word.text)
            onWordRemoved.send { repo.restoreWord(word) }
        }
    }
}