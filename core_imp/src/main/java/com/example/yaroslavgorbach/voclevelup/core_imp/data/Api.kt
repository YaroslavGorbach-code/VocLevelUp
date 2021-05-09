package com.example.yaroslavgorbach.voclevelup.core_imp.data

import com.example.yaroslavgorbach.voclevelup.core_api.data.Def
import com.example.yaroslavgorbach.voclevelup.core_api.data.Language

interface Api {
    suspend fun getDefinitions(words: List<String>, lang: Language): List<Def>
    suspend fun getPredictions(input: String): List<String>
    suspend fun getPronunciations(word: String): List<String>
    suspend fun getCompletions(input: String): List<String>
}