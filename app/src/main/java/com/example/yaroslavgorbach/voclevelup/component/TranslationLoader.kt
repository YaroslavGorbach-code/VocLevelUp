package com.example.yaroslavgorbach.voclevelup.component

import com.example.yaroslavgorbach.voclevelup.data.Definition
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.data.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TranslationLoader(private val repo: Repo) {

    sealed class State {
        data class Success(val result: List<Definition>) : State()
        object Fail : State()
        object Progress : State()
    }

    fun loadTranslation(word: String, lang: Language): Flow<State> =
        flow {
            emit(State.Progress)
            emit(State.Success(repo.getTranslation(word, lang)))
        }.catch { emit(State.Fail) }
}