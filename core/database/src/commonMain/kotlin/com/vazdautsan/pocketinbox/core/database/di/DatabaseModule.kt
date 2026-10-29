package com.vazdautsan.pocketinbox.core.database.di

import androidx.room.RoomDatabase
import com.vazdautsan.pocketinbox.core.database.PocketInBoxDatabase
import com.vazdautsan.pocketinbox.core.database.buildDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule = module {
    includes(platformDatabaseModule)
    single { get<RoomDatabase.Builder<PocketInBoxDatabase>>().buildDatabase() }
    single { get<PocketInBoxDatabase>().noteDao() }
}

/** Provides `RoomDatabase.Builder<PocketInBoxDatabase>` for the current platform. */
internal expect val platformDatabaseModule: Module
