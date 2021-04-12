package com.example.yaroslavgorbach.voclevelup.component

import androidx.core.text.trimmedLength
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface AddWordModel {
    val translation: LiveData<Translation.State?>
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

    private val transFeature = Translation(repo)
    private val wordInput = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)

    override val translation: LiveData<Translation.State?> =
        wordInput
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(400)
            .combine(repo.getTargetLang()) { input, _ -> input }
            .flatMapLatest {
                if (it.length > 1) transFeature.getTranslation(it) else flowOf(null)
            }
            .asLiveData()

    override val saveEnabled: LiveData<Boolean> =
        combine(wordInput, isLoading){ input, loading ->
            input.trimmedLength() > 1 && !loading
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