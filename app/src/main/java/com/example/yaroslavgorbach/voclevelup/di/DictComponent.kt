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
        fun create(
            @BindsInstance f: DictFragment,
            appComponent: AppComponent
        ): DictComponent
    }
}

@InternalCoroutinesApi
@Module
class DictModule {
    @Provides
    fun provideDict(f: DictFragment, repo: Repo): Dictionary {
        val vm = ViewModelProvider(f)[DictViewModel::class.java]
        return vm.dictionary ?: DictionaryImp(repo, vm.viewModelScope).also { vm.dictionary = it }
    }
}

class DictViewModel : ViewModel() {
    var dictionary: Dictionary? = null
}