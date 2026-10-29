package com.vazdautsan.pocketinbox.composeapp.di

import com.vazdautsan.pocketinbox.core.data.di.dataModule
import com.vazdautsan.pocketinbox.feature.notes.di.notesModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/** Starts the app-wide Koin graph. Called once per process from the platform entry point. */
fun startKoinApp(platformConfig: KoinAppDeclaration = {}) {
    startKoin {
        platformConfig()
        modules(dataModule, notesModule)
    }
}
