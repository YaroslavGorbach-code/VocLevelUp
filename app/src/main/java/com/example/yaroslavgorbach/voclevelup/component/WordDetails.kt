package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.toStateFlow
import com.example.yaroslavgorbach.voclevelup.util.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface WordDetails {
    val text: LiveData<String>
    val translations: LiveData<List<String>?>
    val onTransDeleted: LiveEvent<() -> Unit>
    fun onReorderTrans(newTrans: List<String>)
    fun onAddTrans(text: String)
    fun onEditTrans(trans: String, newText: String)
    fun onDeleteTrans(trans: String)
    val pron: LiveData<String>

}

@InternalCoroutinesApi
class WordDetailsImp(
    private val wordText: String,
    private val repo: Repo,
    private val scope: CoroutineScope
) : WordDetails {

    private val word = repo.getWord(wordText).filterNotNull().toStateFlow(scope)

    override val text = word.mapNotNull { it?.text }.onStart { emit(wordText) }.asLiveData()
    override val translations = word.map { it?.translations }.onStart { emit(null) }.asLiveData()
    override val onTransDeleted = MutableLiveEvent<() -> Unit>()

    override fun onReorderTrans(newTrans: List<String>) {
        scope.launch {
            repo.setTranslations(wordText, newTrans)
        }
    }

    override fun onAddTrans(text: String) {
        scope.launch {
            translations.value?.let {
                repo.setTranslations(wordText, it + text)
            }
        }
    }

    override fun onDeleteTrans(trans: String) {
        val currentTrans = translations.value!!
        val newTrans = currentTrans.toMutableList().apply { remove(trans) }
        scope.launch {
            repo.setTranslations(wordText, newTrans)
            onTransDeleted.send{
                scope.launch {
                    repo.setTranslations(wordText, currentTrans)
                }
            }
        }
    }

    override val pron = word.map { it?.pron ?: "" }.asLiveData()

    override fun onEditTrans(trans: String, newText: String) {
        val currentTrans = translations.value!!
        val newTrans = currentTrans.toMutableList().apply {
            set(indexOfFirst { it == trans }, newText)
        }
        scope.launch {
            repo.setTranslations(wordText, newTrans)
        }
    }
}