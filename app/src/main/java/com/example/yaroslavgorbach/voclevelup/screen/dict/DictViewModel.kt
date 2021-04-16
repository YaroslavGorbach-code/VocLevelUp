package com.example.yaroslavgorbach.voclevelup.screen.dict
import com.example.yaroslavgorbach.voclevelup.component.Dictionary
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.DictionaryImp
import com.example.yaroslavgorbach.voclevelup.repo

class DictViewModel : ViewModel() {
    val dictionary: Dictionary = DictionaryImp(repo, viewModelScope)
}