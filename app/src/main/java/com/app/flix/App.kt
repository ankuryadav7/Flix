package com.app.flix

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.app.flix.di.AppInjector

class App : Application() {
    companion object {
        /** Need Single ApplicationClass context so static field leak is ignored */
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context

        @Synchronized
        fun getContext(): Context {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        /** inject Koin */
        AppInjector.start(this)
        mContext = applicationContext
    }
}