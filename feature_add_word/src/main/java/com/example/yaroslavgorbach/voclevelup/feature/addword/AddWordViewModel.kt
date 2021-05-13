package com.example.yaroslavgorbach.voclevelup.feature.addword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AddWordViewModel(app: Application) : AndroidViewModel(app) {
    val addWordComponent = DaggerAddWordComponent.factory().create(this, app as RepoProvider)
}