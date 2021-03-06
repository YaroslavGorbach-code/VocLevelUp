package com.example.yaroslavgorbach.voclevelup.data.imp

import com.example.yaroslavgorbach.voclevelup.data.api.Word
import kotlinx.coroutines.flow.Flow

interface Db {
    fun getAllWords(): Flow<List<Word>>
    fun getWord(text: String): Flow<Word?>
    suspend fun addWord(text: String, trans: List<String>, pron: String?)
    suspend fun restoreWord(word: Word)
    suspend fun deleteWord(text: String)
    suspend fun updateTranslations(word: String, trans: List<String>)
}