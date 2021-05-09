package com.example.yaroslavgorbach.voclevelup.di

import android.content.Context
import com.example.yaroslavgorbach.voclevelup.data.FakeApi
import com.example.yaroslavgorbach.voclevelup.data.FakeDb
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.RepoImp
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {

    fun provideRepo(): Repo

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideRepo(context: Context): Repo = RepoImp(context, FakeDb, FakeApi)

}
