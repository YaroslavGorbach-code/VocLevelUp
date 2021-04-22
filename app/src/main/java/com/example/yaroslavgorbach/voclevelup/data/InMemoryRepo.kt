package com.example.yaroslavgorbach.voclevelup.data

import androidx.core.os.LocaleListCompat
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
                Word(
                    "Vocup!", listOf("Приложение", "Для запоминания", "Новых", "Слов"),
                    System.nanoTime(), null
                ),
                Word(
                    "World", listOf("Мир", "Вселенная", "Общество", "Свет"),
                    System.nanoTime() + 1, "wərld"
                ),
                Word(
                    "Hello", listOf("Привет", "Здравствуй", "Алло"),
                    System.nanoTime() + 2, "həˈlō"
                )
            )
        }
        GlobalScope.launch {
            getTargetLangPref()?.let {
                targetLang.value = it
            }
        }
    }

    override fun getAllWords() = words.filterNotNull().map { it.sortedBy(Word::created).reversed() }

    override fun getWord(text: String): Flow<Word?> =
        getAllWords()
            .map { words ->
                words.find { it.text == text }
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
        if (Random.nextInt() % 4 == 0) {
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

    override suspend fun setTranslations(word: String, trans: List<String>) {
        withWord(word) { currentWords, wordIndex ->
        words.value = currentWords.toMutableList().apply {
                set(wordIndex, get(wordIndex).copy(translations = trans))
            }
        }
    }

    private fun withWord(word: String, action: (List<Word>, Int) -> Unit) {
        val currentWords = words.value
        if (currentWords != null) {
            val wordIndex = currentWords.indexOfFirst { it.text == word }
            if (wordIndex != -1) {
                action(currentWords, wordIndex)
            }
        }
    }

    override suspend fun addWord(def: Def) =
        addWordInner(def.text, def.translations, System.nanoTime(), loadPron(def.text))

    override suspend fun addWord(word: Word) =
        addWordInner(word.text, word.translations, word.created, word.pron)

    private suspend fun loadPron(word: String): String? =
        if (Random.nextBoolean()) word else null

    private fun addWordInner(
        text: String, translations: List<String>, created: Long, pron: String?
    ) {
        words.value = words.value?.let { listOf(Word(text, translations, created, pron)) + it }
    }

    override suspend fun removeWord(def: Def) = removeWordInner(def.text)
    override suspend fun removeWord(word: Word) = removeWordInner(word.text)

    private fun removeWordInner(text: String) {
        words.value = words.value?.filter { it.text != text }
    }
}