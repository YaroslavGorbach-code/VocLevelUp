package com.example.yaroslavgorbach.voclevelup.data

import com.example.yaroslavgorbach.voclevelup.data.api.Repo

interface RepoProvider {
    fun provideRepo(): Repo
}