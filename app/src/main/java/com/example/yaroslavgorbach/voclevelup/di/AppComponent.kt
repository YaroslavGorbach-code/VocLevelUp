package com.example.yaroslavgorbach.voclevelup.di

import com.example.yaroslavgorbach.voclevelup.core.data.RepoProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [RepoProvider::class])
interface AppComponent : RepoProvider {

    @Component.Factory
    interface Factory {
        fun create(repoProvider: RepoProvider): AppComponent
    }
}