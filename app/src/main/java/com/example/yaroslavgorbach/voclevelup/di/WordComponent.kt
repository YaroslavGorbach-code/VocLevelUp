package com.example.yaroslavgorbach.voclevelup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.WordDetails
import com.example.yaroslavgorbach.voclevelup.component.WordDetailsImp
import com.example.yaroslavgorbach.voclevelup.core.data.Repo
import com.example.yaroslavgorbach.voclevelup.screen.word.WordFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ViewModelScope
@Component(dependencies = [AppComponent::class], modules = [WordModule::class])
interface WordComponent {
    fun inject(f: WordFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance vm: ViewModel, @BindsInstance word: String, appComponent: AppComponent): WordComponent
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