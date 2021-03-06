package com.example.yaroslavgorbach.voclevelup.feature.dictionary.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.data.api.Repo
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.model.Dictionary
import com.example.yaroslavgorbach.voclevelup.feature.ViewModelScope
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.DictFragment
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.model.DictionaryImp
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
@ViewModelScope
@Component(dependencies = [RepoProvider::class], modules = [DictModule::class])
internal interface DictComponent {

    fun inject(f: DictFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance vm: ViewModel, repoProvider: RepoProvider): DictComponent
    }
}

@Module
internal class DictModule {

    @ViewModelScope
    @Provides
    fun provideDict(vm: ViewModel, repo: Repo): Dictionary = DictionaryImp(repo, vm.viewModelScope)
}