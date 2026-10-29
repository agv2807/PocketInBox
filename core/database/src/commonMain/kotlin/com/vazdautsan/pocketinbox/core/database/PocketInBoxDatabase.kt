package com.vazdautsan.pocketinbox.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.vazdautsan.pocketinbox.core.database.dao.NoteDao
import com.vazdautsan.pocketinbox.core.database.model.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [NoteEntity::class], version = 1)
@ConstructedBy(PocketInBoxDatabaseConstructor::class)
abstract class PocketInBoxDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

// Room generates the actual implementations
@Suppress("KotlinNoActualForExpect")
expect object PocketInBoxDatabaseConstructor : RoomDatabaseConstructor<PocketInBoxDatabase> {
    override fun initialize(): PocketInBoxDatabase
}

internal const val DATABASE_NAME = "pocketinbox.db"

/** Common configuration on top of a platform-specific builder. */
internal fun RoomDatabase.Builder<PocketInBoxDatabase>.buildDatabase(): PocketInBoxDatabase =
    setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
