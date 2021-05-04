package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
class AddWordViewModel(app: Application) : AndroidViewModel(app) {
    @InternalCoroutinesApi
    val addWord: AddWord = AddWordImp(repo, viewModelScope)
}