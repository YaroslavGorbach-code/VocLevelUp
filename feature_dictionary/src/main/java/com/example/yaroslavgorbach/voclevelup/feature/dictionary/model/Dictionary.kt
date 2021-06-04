package com.example.yaroslavgorbach.voclevelup.feature.dictionary.model

import androidx.lifecycle.LiveData
import com.example.yaroslavgorbach.voclevelup.data.api.Word
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent

internal interface Dictionary {
    val words: LiveData<List<Word>>
    val loading: LiveData<Boolean>
    val onWordRemoved: LiveEvent<suspend () -> Unit>

    fun onRemove(word: Word)
}

