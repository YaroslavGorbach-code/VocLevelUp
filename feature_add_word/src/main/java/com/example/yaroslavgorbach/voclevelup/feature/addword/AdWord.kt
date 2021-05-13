package com.example.yaroslavgorbach.voclevelup.feature.addword

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.*
import com.example.yaroslavgorbach.voclevelup.feature.addword.AddWord.*
import com.example.yaroslavgorbach.voclevelup.data.Def
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.util.repeatWhen
import com.example.yaroslavgorbach.voclevelup.util.toStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.concurrent.TimeUnit

interface AddWord {
    val state: LiveData<State>
    val maxWordLength: Int
    val languages: LiveData<List<Language>>
    fun onInput(text: String)
    fun onSave(item: DefItem)
    fun onChooseLang(lang: Language)
    fun onRetry()
    fun onSearch(text: String)

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Definitions(val items: List<DefItem>, val error: Boolean) : State()
        data class Completions(val items: List<String>) : State()
    }

    data class DefItem(val text: String, val saved: Boolean, val trans: List<Pair<String, Boolean>>?)
}

@InternalCoroutinesApi
class AddWordImp(
    private val repo: Repo,
    private val scope: CoroutineScope
) : AddWord {

    private sealed class Action {
        data class Input(val text: String) : Action()
        data class Search(val text: String) : Action()
    }

    companion object {
        private val WORD_RANGE = 2..30
    }

    private val actions = Channel<Action>()
    private val allWords = repo.getAllWords().toStateFlow(scope)
    private val retry = Channel<Unit>()

    override val state =
        actions.receiveAsFlow()
            .distinctUntilChanged()
            .flatMapLatest {
                when (it) {
                    is Action.Input -> completionFlow(normalizeInput(it.text))
                    is Action.Search -> definitionFlow(normalizeInput(it.text))
                }
            }
            .onStart { emit(AddWord.State.Idle) }
            .asLiveData(timeoutInMs = TimeUnit.MINUTES.toMillis(5))

    private fun normalizeInput(input: String) = input.trim().replace(Regex("\\s+"), " ")

    private fun completionFlow(input: String): Flow<State> = flow {
        if (input.length >= WORD_RANGE.first) {
            val comp = repo.getCompletions(input)
            if (comp.isNotEmpty()) {
                emit(State.Completions(comp))
            } else {
                emit(State.Completions(listOf(input)))
            }
        } else {
            emit(State.Completions(emptyList()))
        }
    }

    private fun defItemsFlow(input: String, loadDefResult: List<Def>?): Flow<List<DefItem>> =
        allWords
            .filterNotNull()
            .map { words ->
                val defs = if (!loadDefResult.isNullOrEmpty()) loadDefResult else listOf(Def(input, emptyList()))
                defs.map { def ->
                    val savedWord = words.find { it.text == def.text }
                    if (loadDefResult != null) {
                        if (savedWord != null) {
                            DefItem(def.text, true, def.translations.map {
                                it to savedWord.translations.contains(it)
                            })
                        } else {
                            DefItem(def.text, false, def.translations.map { it to false })
                        }
                    } else {
                        DefItem(def.text, savedWord != null, null)
                    }
                }
            }

    private fun definitionFlow(input: String): Flow<State> =
        repo.getTargetLanguage()
            .repeatWhen(retry.receiveAsFlow())
            .transformLatest { lang ->
                if (input.length in WORD_RANGE) {
                    emit(State.Loading)
                    val result = try {
                        repo.getDefinitions(input, lang)
                    } catch (e: IOException) {
                        null
                    }
                    emitAll(defItemsFlow(input, result).map {
                        State.Definitions(
                            it,
                            result == null
                        )
                    })
                } else {
                    emit(State.Idle)
                }
            }

    override val maxWordLength = WORD_RANGE.last

    override val languages: LiveData<List<Language>> =
        repo.getTargetLanguage().map {
            listOf(it) + (Language.values().toList() - it)
        }.asLiveData()

    override fun onInput(text: String) {
        actions.offer(Action.Input(text))
    }

    override fun onSave(item: DefItem) {
        scope.launch {
            if (!item.saved) {
                repo.addWord(item.text, item.trans?.map { it.first } ?: emptyList())
            } else {
                repo.addTranslations(
                    item.text, (item.trans ?: emptyList()).filter { !it.second }.map { it.first }
                )
            }
        }
    }

    override fun onChooseLang(lang: Language) {
        scope.launch {
            repo.setTargetLanguage(lang)
        }
    }

    override fun onRetry() {
        retry.offer(Unit)
    }

    override fun onSearch(text: String) {
        actions.offer(Action.Search(text))
    }
}