package com.example.yaroslavgorbach.voclevelup.data

import androidx.core.os.LocaleListCompat
import androidx.lifecycle.MutableLiveData
import com.example.yaroslavgorbach.voclevelup.util.move
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random

object InMemoryRepo : Repo {

    private val words: MutableStateFlow<List<Word>?> = MutableStateFlow(null)
    private val targetLang = MutableStateFlow(suggestTargetLang())

    init {
        GlobalScope.launch {
            delay(1000)
            words.value = listOf(
                Word("World", listOf("Мир", "Вселенная", "Общество", "Свет"), System.nanoTime()),
                Word("Hello", listOf("Привет", "Здравствуй", "Алло"), System.nanoTime() + 1)
            )
        }
        GlobalScope.launch {
            getTargetLangPref()?.let {
                targetLang.value = it
            }
        }
    }

    override fun getAllWords() = words.filterNotNull().map { it.sortedBy(Word::created).reversed() }

    override suspend fun getWord(text: String): Word? {
        delay(1000)
        return words.value?.find { it.text == text }
    }

    override fun getTargetLang(): Flow<Language> = targetLang

    private suspend fun getTargetLangPref(): Language? {
        delay(3000)
        return null
    }

    private fun suggestTargetLang(): Language {
        val userLocales = LocaleListCompat.getAdjustedDefault()
        for (i in 0 until userLocales.size()) {
            val supportedLang = Language.values().find { it.code == userLocales[i].language }
            if (supportedLang != null) {
                return supportedLang
            }
        }
        return Language.Russian
    }

    override suspend fun setTargetLang(lang: Language) {
        targetLang.value = lang
    }

    override suspend fun getTranslations(word: String, lang: Language): List<Def> {
        delay(1000)
        if (Random.nextInt(5) == 0) {
            if (Random.nextBoolean()) {
                throw IOException("Can't load translation")
            } else {
                return emptyList()
            }
        }
        return List(Random.nextInt(1, 5)) { defIndex ->
            Def("$word $defIndex (${lang.code})",
                List(Random.nextInt(1, 10)) { transIndex ->
                    "Translation $transIndex"
                }
            )
        }
    }

    override suspend fun moveTrans(wordText: String, from: Int, to: Int) {
        val currentWords = words.value!!
        val wordIndex = currentWords.indexOfFirst { it.text == wordText }
        if (wordIndex != -1) {
            val word = currentWords[wordIndex]
            val newTrans = word.translations.move(from, to)
            val newWords = currentWords.toMutableList().apply {
                set(wordIndex, Word(word.text, newTrans, word.created))
            }
            words.value = newWords
        }
    }

    override suspend fun addWord(def: Def) = addWordInner(def.text, def.translations, System.nanoTime())
    override suspend fun addWord(word: Word) = addWordInner(word.text, word.translations, word.created)

    private fun addWordInner(text: String, translations: List<String>, created: Long) {
        words.value = words.value?.let { listOf(Word(text, translations, created)) + it }
    }

    override suspend fun removeWord(def: Def) = removeWordInner(def.text)
    override suspend fun removeWord(word: Word) = removeWordInner(word.text)

    private fun removeWordInner(text: String) {
        words.value = words.value?.filter { it.text != text }
    }
}