package com.example.yaroslavgorbach.voclevelup

import android.app.Application
import com.example.yaroslavgorbach.voclevelup.data.DaggerRepoComponent
import com.example.yaroslavgorbach.voclevelup.di.DaggerAppComponent


class App : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(DaggerRepoComponent.factory().create(this))
    }
}