package com.example.yaroslavgorbach.voclevelup.feature.dictionary.dict

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.di.DaggerDictComponent
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class DictViewModel(app: Application) : AndroidViewModel(app) {

    val dictComponent = DaggerDictComponent.factory().create(this, app as RepoProvider)

}