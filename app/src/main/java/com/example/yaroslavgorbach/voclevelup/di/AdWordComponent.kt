package com.example.yaroslavgorbach.voclevelup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.component.AddWordImp
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.screen.addword.AddWordFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ViewModelScope
@Component(dependencies = [AppComponent::class], modules = [AddWordModule::class])
interface AddWordComponent {

    fun inject(f: AddWordFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance vm: ViewModel, appComponent: AppComponent): AddWordComponent
    }
}

@InternalCoroutinesApi
@Module
class AddWordModule {
    @ViewModelScope
    @Provides
    fun provideAddWord(vm: ViewModel, repo: Repo): AddWord = AddWordImp(repo, vm.viewModelScope)
}