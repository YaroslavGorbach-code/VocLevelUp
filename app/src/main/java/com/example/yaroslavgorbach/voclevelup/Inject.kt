package com.example.yaroslavgorbach.voclevelup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.yaroslavgorbach.voclevelup.data.InMemoryRepo
import com.example.yaroslavgorbach.voclevelup.data.Repo
import com.example.yaroslavgorbach.voclevelup.screen.nav.Navigation

val Fragment.nav get() = activity as Navigation
val ViewModel.repo: Repo get() = InMemoryRepo

