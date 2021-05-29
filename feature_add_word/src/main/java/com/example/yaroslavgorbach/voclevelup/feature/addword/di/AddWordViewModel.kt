package com.example.yaroslavgorbach.voclevelup.feature.addword.di


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
internal class AddWordViewModel(app: Application) : AndroidViewModel(app) {
    val addWordComponent = DaggerAddWordComponent.factory().create(this, app as RepoProvider)
}