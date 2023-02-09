package com.app.flix.di

import com.app.flix.list.implementation.ListRepoImpl
import com.app.flix.list.viewModel.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModels = module {
    viewModel { ListViewModel(listRepoImpl = get()) }
}

val repositoryModule = module {
    factory { ListRepoImpl() }
}