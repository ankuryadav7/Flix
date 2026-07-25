package com.app.flix.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AppInjector {
    companion object {
        /**
         * Should be called from Application.onCreate()
         */
        
        fun start(application: Application) = startKoin {
            
            modules(listOf(viewModels, repositoryModule))
        }
    }
}
