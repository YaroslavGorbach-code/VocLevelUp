package com.example.yaroslavgorbach.voclevelup.feature.worddetails.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.data.api.Repo
import com.example.yaroslavgorbach.voclevelup.feature.ViewModelScope
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.WordFragment
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.model.WordDetails
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.model.WordDetailsImp

@ViewModelScope
@Component(dependencies = [RepoProvider::class], modules = [WordModule::class])
internal interface WordComponent {

    fun inject(f: WordFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vm: ViewModel, @BindsInstance word: String, repoProvider: RepoProvider
        ): WordComponent
    }
}

@Module
internal class WordModule {

    @ViewModelScope
    @Provides
    fun provideDetails(vm: ViewModel, word: String, repo: Repo): WordDetails =
        WordDetailsImp(word, repo, vm.viewModelScope)
}