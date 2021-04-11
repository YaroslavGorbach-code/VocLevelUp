package com.example.yaroslavgorbach.voclevelup.screen.addword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.AddWordImp
import com.example.yaroslavgorbach.voclevelup.component.AddWordModel
import com.example.yaroslavgorbach.voclevelup.repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class AddWordViewModel : ViewModel() {
    val addWord: AddWordModel = AddWordImp(repo, viewModelScope)
}