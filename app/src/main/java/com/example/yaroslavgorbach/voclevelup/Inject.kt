package com.example.yaroslavgorbach.voclevelup

import androidx.lifecycle.ViewModel
import com.example.yaroslavgorbach.voclevelup.data.InMemoryRepo
import com.example.yaroslavgorbach.voclevelup.data.Repo

val ViewModel.repo: Repo get() = InMemoryRepo
