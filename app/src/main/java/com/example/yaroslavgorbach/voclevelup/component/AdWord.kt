package com.example.yaroslavgorbach.voclevelup.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.component.AddWord.TransState.*
import com.example.yaroslavgorbach.voclevelup.component.AddWord.*
import com.example.yaroslavgorbach.voclevelup.data.Trans
import com.example.yaroslavgorbach.voclevelup.util.asStateFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException

interface AddWord {
    val maxWordLength: Int
    val translation: LiveData<TransState>
    val languages: LiveData<List<Language>>
    fun onInput(text: String)
    fun onSave(item: TransItem)
    fun onRemove(item: TransItem)
    fun onChooseLang(lang: Language)

    sealed class TransState {
        object Idle : TransState()
        object Progress : TransState()
        data class Success(val result: List<TransItem>) : TransState()
        object Fail : TransState()
    }

    data class TransItem(val trans: Trans, val saved: Boolean)
}

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class AddWordImp(
    private val repo: Repo,
    private val scope: CoroutineScope
) : AddWord {

    companion object {
        private val WORD_RANGE = 2..30
    }

    private val wordInput = MutableStateFlow("")
    private val allWords = repo.getAllWords().asStateFlow(scope)

    override val translation: LiveData<TransState> =
        wordInput
            .map { normalizeInput(it) }
            .combine(repo.getTargetLang()) { input, lang -> input to lang }
            .transformLatest { (input, lang) ->
                if (input.length in WORD_RANGE) {
                    emit(Progress)
                    delay(400)
                    val result = try {
                        repo.getTranslations(input, lang)
                    } catch (e: IOException) {
                        null
                    }
                    if (result != null) {
                        emitAll(allWords.filterNotNull().map { savedWords ->
                            Success(result.map { trans ->
                                TransItem(trans, savedWords.any { it.trans.text == trans.text })
                            })
                        })
                    } else {
                        emit(Fail)
                    }
                } else {
                    emit(Idle)
                }
            }
            .asLiveData()

    override val maxWordLength = WORD_RANGE.last

    private fun normalizeInput(input: String) =
        input.trim().replace(Regex("\\s+"), " ")


    override val languages: LiveData<List<Language>> =
        repo.getTargetLang().map {
            listOf(it) + (Language.values().toList() - it)
        }.asLiveData()


    override fun onInput(text: String) {
        wordInput.value = text
    }

    override fun onSave(item: TransItem) {
        scope.launch {
            repo.addWord(item.trans)
        }
    }

    override fun onRemove(item: TransItem) {
        scope.launch {
            repo.removeWord(item.trans)
        }
    }

    override fun onChooseLang(lang: Language) {
        scope.launch {
            repo.setTargetLang(lang)
        }
    }
}