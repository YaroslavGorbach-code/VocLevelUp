package com.example.yaroslavgorbach.voclevelup

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.yaroslavgorbach.voclevelup.data.FakeApi
import com.example.yaroslavgorbach.voclevelup.data.FakeDb
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.data.RepoImp

val AndroidViewModel.repo: Repo get() = RepoImp(getApplication(), FakeDb, FakeApi)
