package com.example.yaroslavgorbach.voclevelup.screen.dict
import com.example.yaroslavgorbach.voclevelup.component.WordList
import androidx.lifecycle.ViewModel
import com.example.yaroslavgorbach.voclevelup.component.WordListImp
import com.example.yaroslavgorbach.voclevelup.repo

class DictViewModel : ViewModel() {
    val wordList: WordList = WordListImp(repo)
}