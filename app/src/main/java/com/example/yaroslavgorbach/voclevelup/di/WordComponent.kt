package com.example.yaroslavgorbach.voclevelup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.WordDetails
import com.example.yaroslavgorbach.voclevelup.component.WordDetailsImp
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.screen.word.WordFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [WordModule::class])
interface WordComponent {
    fun inject(f: WordFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance f: WordFragment,
            @BindsInstance word: String,
            appComponent: AppComponent
        ): WordComponent
    }
}

@InternalCoroutinesApi
@Module
class WordModule {
    @Provides
    fun provideDetails(f: WordFragment, word: String, repo: Repo): WordDetails {
        val vm = ViewModelProvider(f)[WordViewModel::class.java]
        return vm.wordDetails ?: WordDetailsImp(
            word,
            repo,
            vm.viewModelScope
        ).also { vm.wordDetails = it }
    }
}

class WordViewModel : ViewModel() {
    var wordDetails: WordDetails? = null
}