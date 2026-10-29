package com.vazdautsan.pocketinbox.core.data.repository

import app.cash.turbine.test
import com.vazdautsan.pocketinbox.core.database.dao.NoteDao
import com.vazdautsan.pocketinbox.core.database.model.NoteEntity
import com.vazdautsan.pocketinbox.core.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class OfflineNotesRepositoryTest {

    private val now = Instant.fromEpochMilliseconds(1_700_000_000_000)
    private val dao = FakeNoteDao()
    private val repository = OfflineNotesRepository(
        noteDao = dao,
        clock = object : Clock {
            override fun now() = now
        },
    )

    @Test
    fun addNoteStoresTextWithCurrentTime() = runTest {
        repository.addNote("Buy milk")

        assertEquals(listOf(NoteEntity(id = 1, text = "Buy milk", createdAt = now.toEpochMilliseconds())), dao.notes.value)
    }

    @Test
    fun observeNotesMapsEntitiesToDomain() = runTest {
        dao.notes.value = listOf(NoteEntity(id = 7, text = "Hello", createdAt = 42))

        repository.observeNotes().test {
            assertEquals(listOf(Note(id = 7, text = "Hello", createdAt = Instant.fromEpochMilliseconds(42))), awaitItem())
        }
    }

    private class FakeNoteDao : NoteDao {
        val notes = MutableStateFlow<List<NoteEntity>>(emptyList())

        override fun observeAll(): Flow<List<NoteEntity>> = notes

        override suspend fun insert(note: NoteEntity): Long {
            val id = notes.value.size + 1L
            notes.update { it + note.copy(id = id) }
            return id
        }
    }
}
