package com.example.yaroslavgorbach.voclevelup.screen.dict
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.component.Dictionary
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.DictionaryImp
import com.example.yaroslavgorbach.voclevelup.repo

class DictViewModel(app: Application) : AndroidViewModel(app) {
    val dictionary: Dictionary = DictionaryImp(repo, viewModelScope)
}