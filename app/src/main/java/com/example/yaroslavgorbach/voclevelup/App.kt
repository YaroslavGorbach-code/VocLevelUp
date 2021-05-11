package com.example.yaroslavgorbach.voclevelup

import android.app.Application
import com.example.yaroslavgorbach.voclevelup.data.DaggerRepoComponent
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.RepoProvider
import com.example.yaroslavgorbach.voclevelup.di.DaggerAppComponent


class App : Application(), RepoProvider {
    private val appComponent by lazy {
        DaggerAppComponent.factory().create(DaggerRepoComponent.factory().create(this))
    }

    override fun provideRepo() = appComponent.provideRepo()
}