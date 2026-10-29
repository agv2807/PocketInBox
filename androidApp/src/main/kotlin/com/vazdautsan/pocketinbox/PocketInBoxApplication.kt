package com.vazdautsan.pocketinbox

import android.app.Application
import com.vazdautsan.pocketinbox.composeapp.di.startKoinApp
import org.koin.android.ext.koin.androidContext

class PocketInBoxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoinApp {
            androidContext(this@PocketInBoxApplication)
        }
    }
}
