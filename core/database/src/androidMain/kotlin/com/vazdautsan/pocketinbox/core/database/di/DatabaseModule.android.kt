package com.vazdautsan.pocketinbox.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.vazdautsan.pocketinbox.core.database.DATABASE_NAME
import com.vazdautsan.pocketinbox.core.database.PocketInBoxDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDatabaseModule: Module = module {
    single<RoomDatabase.Builder<PocketInBoxDatabase>> {
        val context = androidContext()
        Room.databaseBuilder<PocketInBoxDatabase>(
            context = context,
            name = context.getDatabasePath(DATABASE_NAME).absolutePath,
        )
    }
}
