package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.App
import com.example.yaroslavgorbach.voclevelup.di.DaggerAddWordComponent
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AddWordViewModel(app: Application) : AndroidViewModel(app) {
    val addWordComponent = DaggerAddWordComponent.factory()
        .create(this, getApplication<App>().appComponent)
}