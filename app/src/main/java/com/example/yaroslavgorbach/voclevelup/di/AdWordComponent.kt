package com.example.yaroslavgorbach.voclevelup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [AddWordModule::class])
interface AddWordComponent {

    fun inject(f: AddWordFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance f: AddWordFragment,
            appComponent: AppComponent
        ): AddWordComponent
    }
}

@InternalCoroutinesApi
@Module
class AddWordModule {

    @Provides
    fun provideAddWord(f: AddWordFragment, repo: Repo): AddWord {
        val vm = ViewModelProvider(f)[AddWordViewModel::class.java]
        return vm.addWord ?: AddWordImp(repo, vm.viewModelScope).also { vm.addWord = it }
    }
}

class AddWordViewModel : ViewModel() {
    var addWord: AddWord? = null
}