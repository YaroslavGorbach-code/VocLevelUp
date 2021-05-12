package com.example.yaroslavgorbach.voclevelup.feature.dictionary.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.feature.ViewModelScope
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.screen.addword.AddWordFragment
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.component.AddWord
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.component.AddWordImp
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ViewModelScope
@Component(dependencies = [RepoProvider::class], modules = [AddWordModule::class])
interface AddWordComponent {

    fun inject(f: AddWordFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance vm: ViewModel, repoProvider: RepoProvider): AddWordComponent
    }
}

@InternalCoroutinesApi
@Module
class AddWordModule {

    @ViewModelScope
    @Provides
    fun provideAddWord(vm: ViewModel, repo: Repo): AddWord = AddWordImp(repo, vm.viewModelScope)
}