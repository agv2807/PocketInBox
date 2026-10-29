package com.vazdautsan.pocketinbox.core.domain.repository

import com.vazdautsan.pocketinbox.core.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    /** All notes, newest first. Emits again on every change. */
    fun observeNotes(): Flow<List<Note>>

    suspend fun addNote(text: String)
}
