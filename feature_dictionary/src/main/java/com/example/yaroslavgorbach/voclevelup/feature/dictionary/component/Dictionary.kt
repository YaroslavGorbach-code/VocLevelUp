package com.example.yaroslavgorbach.voclevelup.feature.dictionary.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

interface Dictionary {
    val words: LiveData<List<Word>>
    val loading: LiveData<Boolean>
    val onWordRemoved: LiveEvent<suspend () -> Unit>
    fun onRemove(word: Word)

}

class DictionaryImp(
    private val repo: Repo,
    private val scope: CoroutineScope
) : Dictionary {
    override val onWordRemoved = MutableLiveEvent<suspend () -> Unit>()
    override val words: LiveData<List<Word>> = repo.getAllWords().asLiveData()
    override val loading: LiveData<Boolean> =
        repo.getAllWords()
            .map { it as List<Word>? }
            .onStart { emit(null) }
            .map { it == null }
            .asLiveData()

    override fun onRemove(word: Word) {
        scope.launch {
            repo.deleteWord(word.text)
            onWordRemoved.send { repo.restoreWord(word) }
        }
    }
}