package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.asStateFlow
import com.example.yaroslavgorbach.voclevelup.util.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface WordDetails {
    val text: LiveData<String>
    val translations: LiveData<List<String>?>
    fun onEditTrans(trans: String, newText: String)
    fun onReorderTrans(newTrans: List<String>)
    fun onAddTrans(text: String)
}

@InternalCoroutinesApi
class WordDetailsImp(
    private val wordText: String,
    private val repo: Repo,
    private val scope: CoroutineScope
) : WordDetails {

    private val word = repo.getWord(wordText).filterNotNull().asStateFlow(scope)

    override fun onReorderTrans(newTrans: List<String>) {
        scope.launch {
            repo.setTranslations(wordText, newTrans)
        }
    }

    override fun onAddTrans(text: String) {
        scope.launch {
            translations.value?.let {
                repo.setTranslations(wordText, listOf(text) + it)
            }
        }
    }

    override val text = word.mapNotNull { it?.text }.onStart { emit(wordText) }.asLiveData()
            override val translations =
                word.map { it?.translations }.onStart { emit(null) }.asLiveData()

    override fun onEditTrans(trans: String, newText: String) {
        scope.launch {
            translations.value?.let { currentTrans ->
                val newTrans = currentTrans.toMutableList().apply {
                    val index = indexOfFirst { it == trans }
                    if (newText.isNotBlank()) {
                        set(index, newText)
                    } else {
                        removeAt(index)
                    }
                }
                repo.setTranslations(wordText, newTrans)
            }
        }
    }

}