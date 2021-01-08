package com.example.wast

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WastApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModules = getModules(this)
        startKoin {
            androidLogger()
            androidContext(this@WastApp)
            androidFileProperties()
            modules(appModules)
        }
    }


}