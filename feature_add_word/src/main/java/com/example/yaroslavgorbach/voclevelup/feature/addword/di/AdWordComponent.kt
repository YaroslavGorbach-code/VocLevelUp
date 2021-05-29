package com.example.yaroslavgorbach.voclevelup.feature.addword.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.data.api.Repo
import com.example.yaroslavgorbach.voclevelup.feature.ViewModelScope
import com.example.yaroslavgorbach.voclevelup.feature.addword.AddWordFragment
import com.example.yaroslavgorbach.voclevelup.feature.addword.model.AddWord
import com.example.yaroslavgorbach.voclevelup.feature.addword.model.AddWordImp
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ViewModelScope
@Component(dependencies = [RepoProvider::class], modules = [AddWordModule::class])
internal interface AddWordComponent {

    fun inject(f: AddWordFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance vm: ViewModel, repoProvider: RepoProvider): AddWordComponent
    }
}

@InternalCoroutinesApi
@Module
internal class AddWordModule {

    @ViewModelScope
    @Provides
    fun provideAddWord(vm: ViewModel, repo: Repo): AddWord = AddWordImp(repo, vm.viewModelScope)
}