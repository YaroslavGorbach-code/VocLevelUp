package com.example.yaroslavgorbach.voclevelup.feature.dictionary.addword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.di.DaggerAddWordComponent
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AddWordViewModel(app: Application) : AndroidViewModel(app) {
    val addWordComponent = DaggerAddWordComponent.factory().create(this, app as RepoProvider)
}