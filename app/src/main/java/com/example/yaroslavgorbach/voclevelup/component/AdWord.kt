package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.component.AddWord.*
import com.example.yaroslavgorbach.voclevelup.data.Def
import com.example.yaroslavgorbach.voclevelup.util.combine
import com.example.yaroslavgorbach.voclevelup.util.repeatWhen
import com.example.yaroslavgorbach.voclevelup.util.toStateFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.io.IOException

interface AddWord {
    val definitions: LiveData<DefState>
    val maxWordLength: Int
    val languages: LiveData<List<Language>>
    fun onInput(text: String)
    fun onSave(item: DefItem)
    fun onRemove(item: DefItem)
    fun onChooseLang(lang: Language)
    fun onRetry()

    sealed class DefState {
        object Idle : DefState()
        object Loading : DefState()
        data class Data(val items: List<DefItem>) : DefState()
        data class Error(val items: List<DefItem>) : DefState()
    }

    data class DefItem(val def: Def, val saved: Boolean, val error: Boolean)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AddWordImp(
    private val repo: Repo,
    private val scope: CoroutineScope
) : AddWord {

    companion object {
        private val WORD_RANGE = 2..30
    }

    private val wordInput = MutableStateFlow("")
    private val allWords = repo.getAllWords().toStateFlow(scope)
    private val retry = Channel<Unit>()

    override val definitions: LiveData<DefState> =
        wordInput
            .map { normalizeInput(it) }
            .distinctUntilChanged()
            .combine(repo.getTargetLang())
            .repeatWhen(retry.consumeAsFlow())
            .transformLatest { (input, lang) ->
                if (input.length in WORD_RANGE) {
                    emit(DefState.Loading)
                    delay(400)
                    val result = try {
                        repo.getTranslations(input, lang)
                    } catch (e: IOException) {
                        null
                    }
                    emitAll(allWords.filterNotNull().map { words ->
                        val defs = if (result?.isNotEmpty() == true) result else listOf(Def(input, emptyList()))
                        val items = defs.map { def ->
                            DefItem(def, words.any { it.text == def.text }, result == null)
                        }
                        if (result != null) {
                            DefState.Data(items)
                        } else {
                            DefState.Error(items)
                        }
                    })
                } else {
                    emit(DefState.Idle)
                }
            }
            .asLiveData()

    private fun normalizeInput(input: String) =
        input.trim().replace(Regex("\\s+"), " ")

    override val maxWordLength = WORD_RANGE.last

    override val languages: LiveData<List<Language>> =
        repo.getTargetLang().map {
            listOf(it) + (Language.values().toList() - it)
        }.asLiveData()

    override fun onInput(text: String) {
        wordInput.value = text
    }

    override fun onSave(item: DefItem) {
        scope.launch {
            repo.addWord(item.def)
        }
    }

    override fun onRemove(item: DefItem) {
        scope.launch {
            repo.removeWord(item.def)
        }
    }

    override fun onChooseLang(lang: Language) {
        scope.launch {
            repo.setTargetLang(lang)
        }
    }

    override fun onRetry() {
        retry.offer(Unit)
    }

}