package com.courses.app

import android.app.Application
import com.courses.feature.login.di.loginModule
import com.courses.feature.main.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(loginModule, mainModule)
        }
    }
}