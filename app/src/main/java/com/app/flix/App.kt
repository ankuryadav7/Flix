package com.app.flix

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.app.flix.di.AppInjector
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : Application() {
    companion object {
        /** Need Single ApplicationClass context so static field leak is ignored */
        /** Need Single ApplicationClass context so static field leak is ignored */
        /** Need Single ApplicationClass context so static field leak is ignored */
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        @Synchronized
        fun getContext(): Context {
            return mContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        /** inject Koin */
        /** inject Koin */
        AppInjector.start(this)
        mContext = applicationContext

        GlobalScope.launch {
            val config = fetchRemoteConfig()
            mContext = config.overrideContext
        }
    }

    private suspend fun fetchRemoteConfig(): RemoteConfig {
        return RemoteConfig(overrideContext = null)
    }
}
