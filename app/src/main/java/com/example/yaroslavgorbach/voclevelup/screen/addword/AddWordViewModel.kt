package com.example.yaroslavgorbach.voclevelup.screen.addword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.AddWordImp
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
class AddWordViewModel : ViewModel() {
    @InternalCoroutinesApi
    val addWord: AddWord = AddWordImp(repo, viewModelScope)
}