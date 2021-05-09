package com.example.yaroslavgorbach.voclevelup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.Dictionary
import com.example.yaroslavgorbach.voclevelup.component.DictionaryImp
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.screen.dict.DictFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ViewModelScope
@Component(dependencies = [AppComponent::class], modules = [DictModule::class])
interface DictComponent {
    fun inject(f: DictFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance vm: ViewModel, appComponent: AppComponent): DictComponent
    }
}

@InternalCoroutinesApi
@Module
class DictModule {
    @ViewModelScope
    @Provides
    fun provideDict(vm: ViewModel, repo: Repo): Dictionary = DictionaryImp(repo, vm.viewModelScope)
}