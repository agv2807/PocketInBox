package com.vazdautsan.pocketinbox.core.data.repository

import com.vazdautsan.pocketinbox.core.database.dao.NoteDao
import com.vazdautsan.pocketinbox.core.database.model.NoteEntity
import com.vazdautsan.pocketinbox.core.domain.model.Note
import com.vazdautsan.pocketinbox.core.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.Instant

/** Local-only implementation backed by Room (see ADR-004). */
internal class OfflineNotesRepository(
    private val noteDao: NoteDao,
    private val clock: Clock,
) : NotesRepository {

    override fun observeNotes(): Flow<List<Note>> =
        noteDao.observeAll().map { entities -> entities.map(NoteEntity::toDomain) }

    override suspend fun addNote(text: String) {
        noteDao.insert(
            NoteEntity(
                text = text,
                createdAt = clock.now().toEpochMilliseconds(),
            )
        )
    }
}

private fun NoteEntity.toDomain() = Note(
    id = id,
    text = text,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
)
