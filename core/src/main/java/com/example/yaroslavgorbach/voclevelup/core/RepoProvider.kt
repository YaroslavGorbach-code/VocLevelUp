package com.example.yaroslavgorbach.voclevelup.core

import com.example.yaroslavgorbach.voclevelup.core_api.data.Repo

interface RepoProvider {
    fun provideRepo(): Repo
}