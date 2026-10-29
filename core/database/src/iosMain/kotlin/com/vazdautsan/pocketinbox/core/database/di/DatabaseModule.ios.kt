package com.vazdautsan.pocketinbox.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.vazdautsan.pocketinbox.core.database.DATABASE_NAME
import com.vazdautsan.pocketinbox.core.database.PocketInBoxDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual val platformDatabaseModule: Module = module {
    single<RoomDatabase.Builder<PocketInBoxDatabase>> {
        Room.databaseBuilder<PocketInBoxDatabase>(name = "${documentDirectory()}/$DATABASE_NAME")
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val url = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(url?.path) { "Documents directory is unavailable" }
}
