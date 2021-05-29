package com.example.yaroslavgorbach.voclevelup.feature.dictionary.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.di.DaggerDictComponent
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
internal class DictViewModel(app: Application) : AndroidViewModel(app) {

    val dictComponent = DaggerDictComponent.factory().create(this, app as RepoProvider)

}