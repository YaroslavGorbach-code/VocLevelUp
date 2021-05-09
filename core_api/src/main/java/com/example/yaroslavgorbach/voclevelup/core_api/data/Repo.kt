package com.example.yaroslavgorbach.voclevelup.core_api.data

import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

interface Repo {
    fun getAllWords(): Flow<List<Word>>
    fun getWord(text: String): Flow<Word?>
    fun getTargetLanguage(): Flow<Language>
    suspend fun getDefinitions(word: String, lang: Language): List<Def>
    suspend fun getCompletions(word: String): List<String>
    suspend fun addWord(text: String, trans: List<String>)
    suspend fun restoreWord(word: Word)
    suspend fun deleteWord(text: String)
    suspend fun setTargetLanguage(lang: Language)
    suspend fun updateTranslations(word: String, trans: List<String>)
    suspend fun addTranslations(word: String, trans: List<String>)
}

data class Word(
    val text: String,
    val translations: List<String>,
    val pron: String?,
    val progress: Int = Random.nextInt(100),
    val created: Long = System.nanoTime()
)

data class Def(val text: String, val translations: List<String>)

enum class Language(val code: String, val nativeName: String) {
    Afrikaans("af", "Afrikaans"),
    Arabic("ar", "العربية"),
    Bulgarian("bg", "Български"),
    Bangla("bn", "বাংলা"),
    BosnianLatin("bs", "bosanski (latinica)"),
    Catalan("ca", "Català"),
    ChineseSimplified("zh-Hans", "简体中文"),
    Czech("cs", "Čeština"),
    Welsh("cy", "Welsh"),
    Danish("da", "Dansk"),
    German("de", "Deutsch"),
    Greek("el", "Ελληνικά"),
    Spanish("es", "Español"),
    Estonian("et", "Eesti"),
    Persian("fa", "Persian"),
    Finnish("fi", "Suomi"),
    Faroese("ht", "Haitian Creole"),
    French("fr", "Français"),
    Hebrew("he", "עברית"),
    Hindi("hi", "हिंदी"),
    Croatian("hr", "Hrvatski"),
    Hungarian("hu", "Magyar"),
    Indonesian("id", "Indonesia"),
    Icelandic("is", "Íslenska"),
    Italian("it", "Italiano"),
    Japanese("ja", "日本語"),
    Korean("ko", "한국어"),
    Lithuanian("lt", "Lietuvių"),
    Latvian("lv", "Latviešu"),
    Maltese("mt", "Il-Malti"),
    Malay("ms", "Melayu"),
    HmongDaw("mww", "Hmong Daw"),
    Dutch("nl", "Nederlands"),
    Norwegian("nb", "Norsk"),
    Polish("pl", "Polski"),
    PortugueseBrazil("pt", "Português (Brasil)"),
    Romanian("ro", "Română"),
    Russian("ru", "Русский"),
    Slovak("sk", "Slovenčina"),
    Slovenian("sl", "Slovenščina"),
    SerbianLatin("sr-Latn", "srpski (latinica)"),
    Swedish("sv", "Svenska"),
    Swahili("sw", "Kiswahili"),
    Tamil("ta", "தமிழ்"),
    Thai("th", "ไทย"),
    KlingonLatin("tlh-Latn", "Klingon (Latin)"),
    Turkish("tr", "Türkçe"),
    Ukrainian("uk", "Українська"),
    Urdu("ur", "اردو"),
    Vietnamese("vi", "Tiếng Việt")
}