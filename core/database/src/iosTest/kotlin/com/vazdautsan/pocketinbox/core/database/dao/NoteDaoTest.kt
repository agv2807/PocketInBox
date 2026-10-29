package com.vazdautsan.pocketinbox.core.database.dao

import androidx.room.Room
import app.cash.turbine.test
import com.vazdautsan.pocketinbox.core.database.PocketInBoxDatabase
import com.vazdautsan.pocketinbox.core.database.buildDatabase
import com.vazdautsan.pocketinbox.core.database.model.NoteEntity
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// Runs on the iOS simulator: the bundled SQLite driver has no natives for Android host tests.
class NoteDaoTest {

    private lateinit var database: PocketInBoxDatabase
    private lateinit var dao: NoteDao

    @BeforeTest
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder<PocketInBoxDatabase>().buildDatabase()
        dao = database.noteDao()
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun observeAllReturnsNewestFirst() = runTest {
        dao.insert(NoteEntity(text = "old", createdAt = 1_000))
        dao.insert(NoteEntity(text = "new", createdAt = 2_000))

        dao.observeAll().test {
            assertEquals(listOf("new", "old"), awaitItem().map { it.text })
        }
    }

    @Test
    fun insertEmitsUpdatedList() = runTest {
        dao.observeAll().test {
            assertEquals(emptyList(), awaitItem())

            dao.insert(NoteEntity(text = "first", createdAt = 1_000))

            assertEquals(listOf("first"), awaitItem().map { it.text })
        }
    }
}
