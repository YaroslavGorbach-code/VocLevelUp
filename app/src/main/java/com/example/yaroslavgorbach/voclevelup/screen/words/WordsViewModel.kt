package com.example.yaroslavgorbach.voclevelup.screen.words
import com.example.yaroslavgorbach.voclevelup.component.WordList
import androidx.lifecycle.ViewModel
import com.example.yaroslavgorbach.voclevelup.component.WordListImp
import com.example.yaroslavgorbach.voclevelup.repo

class WordsViewModel : ViewModel() {
    val wordList: WordList = WordListImp(repo)
}