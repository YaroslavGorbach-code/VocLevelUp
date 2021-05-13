package com.example.yaroslavgorbach.voclevelup.feature.worddetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.feature.ViewModelScope
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ViewModelScope
@Component(dependencies = [RepoProvider::class], modules = [WordModule::class])
interface WordComponent {

    fun inject(f: WordFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vm: ViewModel, @BindsInstance word: String, repoProvider: RepoProvider
        ): WordComponent
    }
}

@InternalCoroutinesApi
@Module
class WordModule {

    @ViewModelScope
    @Provides
    fun provideDetails(vm: ViewModel, word: String, repo: Repo): WordDetails =
        WordDetailsImp(word, repo, vm.viewModelScope)
}