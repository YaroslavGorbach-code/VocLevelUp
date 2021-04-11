package com.example.yaroslavgorbach.voclevelup.screen.addword

import androidx.core.text.trimmedLength
import androidx.lifecycle.*
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface AddWordVm {
    val translation: LiveData<TranslationFeature.State?>
    val saveEnabled: LiveData<Boolean>
    val onWordAdded: ReceiveChannel<String>
    fun onWordInput(text: String)
    fun onSave()
}

@ExperimentalCoroutinesApi
@FlowPreview
class AddWordVmImp : ViewModel(), AddWordVm {
    private val transFeature = TranslationFeature(repo)
    private val wordInput = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)

    override val translation:LiveData<TranslationFeature.State?> =
        wordInput
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(400)
            .flatMapLatest {
                if (it.length > 1) transFeature.getTranslation(it) else flowOf(null)
            }
            .asLiveData()

    override val saveEnabled: LiveData<Boolean> =
        combine(wordInput, isLoading){ input, loading ->
            input.trimmedLength() > 1 && !loading
        }.asLiveData()

    override val onWordAdded = Channel<String>(Channel.UNLIMITED)

    override fun onWordInput(text: String) {
        wordInput.value = text
    }

    override fun onSave() {
        isLoading.value = true
        val wordText = requireNotNull(wordInput.value)
        viewModelScope.launch {
            repo.addWord(wordText)
            onWordAdded.send(wordText)
        }
    }
}