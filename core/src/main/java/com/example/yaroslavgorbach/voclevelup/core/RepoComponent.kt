package com.example.yaroslavgorbach.voclevelup.core

import android.content.Context
import com.example.yaroslavgorbach.voclevelup.core_api.data.Repo
import com.example.yaroslavgorbach.voclevelup.core_imp.data.FakeApi
import com.example.yaroslavgorbach.voclevelup.core_imp.data.FakeDb
import com.example.yaroslavgorbach.voclevelup.core_imp.data.RepoImp
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [RepoModule::class])
interface RepoComponent : RepoProvider {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): RepoComponent
    }
}

@Module
class RepoModule {

    @Singleton
    @Provides
    fun provideRepo(context: Context): Repo = RepoImp(context, FakeDb, FakeApi)
}