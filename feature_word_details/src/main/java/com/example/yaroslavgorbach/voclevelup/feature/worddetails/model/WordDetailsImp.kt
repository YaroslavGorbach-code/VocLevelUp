package com.example.yaroslavgorbach.voclevelup.feature.worddetails.model

import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.api.Repo
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.send
import com.example.yaroslavgorbach.voclevelup.util.toStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class WordDetailsImp(
    private val wordText: String,
    private val repo: Repo,
    private val scope: CoroutineScope
) : WordDetails {

    private val word = repo.getWord(wordText).filterNotNull().toStateFlow(scope)

    override val text =
        word.mapNotNull { it?.text }.onStart { emit(wordText) }.asLiveData(Dispatchers.IO)
    override val pron = word.map { it?.pron ?: "" }.asLiveData(Dispatchers.IO)
    override val translations = word.map { it?.translations }.onStart { emit(null) }
        .asLiveData(Dispatchers.IO)
    override val onTransDeleted = MutableLiveEvent<suspend () -> Unit>()
    override val onWordDeleted = MutableLiveEvent<suspend () -> Unit>()

    override fun onReorderTrans(newTrans: List<String>) {
        scope.launch {
            repo.updateTranslations(wordText, newTrans)
        }
    }

    override fun onAddTrans(text: String) {
        scope.launch {
            translations.value?.let {
                repo.updateTranslations(wordText, it + text)
            }
        }
    }

    override fun onDeleteTrans(trans: String) {
        val currentTrans = translations.value!!
        val newTrans = currentTrans.toMutableList().apply { remove(trans) }
        scope.launch {
            repo.updateTranslations(wordText, newTrans)
            onTransDeleted.send { repo.updateTranslations(wordText, currentTrans) }
        }
    }

    override fun onDeleteWord() {
        word.value?.let { word ->
            scope.launch {
                repo.deleteWord(word.text)
                onWordDeleted.send { repo.restoreWord(word) }
            }
        }
    }

    override fun onEditTrans(trans: String, newText: String) {
        val currentTrans = translations.value!!
        val newTrans = currentTrans.toMutableList().apply {
            set(indexOfFirst { it == trans }, newText)
        }
        scope.launch {
            repo.updateTranslations(wordText, newTrans)
        }
    }
}