package com.example.yaroslavgorbach.voclevelup.feature

import com.example.yaroslavgorbach.voclevelup.data.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class TranslationFeature(private val repo: Repo) {

    sealed class State {
        object Fail : State()
        object Progress : State()
        data class Success(val result: String) : State()
    }

    fun getTranslation(text: String): Flow<State> = flow {
        emit(State.Progress)
        val trans = try {
            repo.getTranslation(text)
        } catch (e: IOException) {
            null
        }
        emit(if (trans != null) State.Success(trans) else State.Fail)
    }
}