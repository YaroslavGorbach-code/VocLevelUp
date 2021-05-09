package com.example.yaroslavgorbach.voclevelup.screen.dict

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.App
import com.example.yaroslavgorbach.voclevelup.di.DaggerDictComponent
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class DictViewModel(app: Application) : AndroidViewModel(app) {
    val dictComponent = DaggerDictComponent.factory()
        .create(this, getApplication<App>().appComponent)
}