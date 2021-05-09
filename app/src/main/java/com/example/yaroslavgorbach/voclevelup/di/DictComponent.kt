package com.example.yaroslavgorbach.voclevelup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yaroslavgorbach.voclevelup.component.Dictionary
import com.example.yaroslavgorbach.voclevelup.component.DictionaryImp
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.screen.dict.DictFragment
import com.example.yaroslavgorbach.voclevelup.util.router
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [DictModule::class])
interface DictComponent {
    fun inject(f: DictFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance f: DictFragment, appComponent: AppComponent): DictComponent
    }
}

@InternalCoroutinesApi
@Module
class DictModule {

    @Provides
    fun provideDict(vm: DictViewModel): Dictionary = vm.dictionary!!

    @Provides
    fun provideRouter(f: DictFragment): DictFragment.Router = f.router()

    @Provides
    fun provideViewModel(f: DictFragment, repo: Repo) = ViewModelProvider(f)[DictViewModel::class.java].apply {
        if (dictionary == null) {
            dictionary = DictionaryImp(repo, viewModelScope)
        }
    }
}

class DictViewModel : ViewModel() {
    var dictionary: Dictionary? = null
}