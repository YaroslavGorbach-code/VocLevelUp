package com.example.yaroslavgorbach.voclevelup.component
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.send
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

interface AddWordModel {
    val translation: LiveData<TranslationLoader.State?>
    val saveEnabled: LiveData<Boolean>
    val onWordAdded: LiveEvent<String>
    val languages: LiveData<List<Language>>
    fun onWordInput(text: String)
    fun onSave()
    fun chooseLang(lang: Language)
}

@ExperimentalCoroutinesApi
@FlowPreview
class AddWordImp(
    private val repo: Repo,
    private val scope: CoroutineScope
): AddWordModel {

    companion object {
        private val WORD_RANGE = 2..20
    }

    private val transFeature = TranslationLoader(repo)
    private val wordInput = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)

    override val translation: LiveData<TranslationLoader.State?> =
        wordInput
            .map { normalizeInput(it) }
            .combine(repo.getTargetLang()) { input, lang -> input to lang }
            .transformLatest { (input, lang) ->
                if (input.length in WORD_RANGE) {
                    emit(TranslationLoader.State.Progress)
                    delay(400)
                    emitAll(transFeature.loadTranslation(input, lang))
                } else {
                    emit(null)
                }
            }
            .asLiveData()

    private fun normalizeInput(input: String) =
        input.trim().replace(Regex("\\s+"), "")

    override val saveEnabled: LiveData<Boolean> =
        combine(wordInput, isLoading){ input, loading ->
            normalizeInput(input).length in WORD_RANGE && !loading
        }.asLiveData()

    override val onWordAdded = MutableLiveEvent<String>()

    override val languages: LiveData<List<Language>> =
        repo.getTargetLang().map {
            listOf(it) + (Language.values().toList() - it)
        }.asLiveData()


    override fun onWordInput(text: String) {
        wordInput.value = text
    }

    override fun onSave() {
        isLoading.value = true
        val wordText = requireNotNull(wordInput.value)
        scope.launch {
            repo.addWord(wordText)
            onWordAdded.send(wordText)
        }
    }

    override fun chooseLang(lang: Language) {
        scope.launch {
            repo.setTargetLang(lang)
        }
    }
}