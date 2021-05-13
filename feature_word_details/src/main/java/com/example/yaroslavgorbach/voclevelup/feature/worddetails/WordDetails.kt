package com.example.yaroslavgorbach.voclevelup.feature.worddetails
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent
import com.example.yaroslavgorbach.voclevelup.util.MutableLiveEvent
import com.example.yaroslavgorbach.voclevelup.util.send
import com.example.yaroslavgorbach.voclevelup.util.toStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface WordDetails {
    val text: LiveData<String>
    val translations: LiveData<List<String>?>
    fun onReorderTrans(newTrans: List<String>)
    fun onAddTrans(text: String)
    fun onEditTrans(trans: String, newText: String)
    fun onDeleteTrans(trans: String)
    val pron: LiveData<String>
    fun onDeleteWord()
    val onTransDeleted: LiveEvent<suspend () -> Unit>
    val onWordDeleted: LiveEvent<suspend () -> Unit>
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

    override val pron = word.map { it?.pron ?: "" }.asLiveData()


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