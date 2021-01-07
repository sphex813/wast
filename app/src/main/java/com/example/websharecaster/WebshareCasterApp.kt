package com.example.websharecaster

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WebshareCasterApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModules = getModules(this)
        startKoin {
            androidLogger()
            androidContext(this@WebshareCasterApp)
            androidFileProperties()
            modules(appModules)
        }
    }


}