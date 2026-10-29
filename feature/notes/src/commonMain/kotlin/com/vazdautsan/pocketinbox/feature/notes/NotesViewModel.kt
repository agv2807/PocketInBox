package com.vazdautsan.pocketinbox.feature.notes

import androidx.lifecycle.viewModelScope
import com.vazdautsan.pocketinbox.core.common.mvi.MviViewModel
import com.vazdautsan.pocketinbox.core.domain.repository.NotesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class NotesViewModel(
    private val notesRepository: NotesRepository,
) : MviViewModel<NotesState, NotesIntent, NotesEffect>(NotesState()) {

    init {
        notesRepository.observeNotes()
            .onEach { notes -> updateState { copy(isLoading = false, notes = notes) } }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: NotesIntent) {
        when (intent) {
            NotesIntent.AddNoteClicked -> updateState { copy(isAddDialogVisible = true, draft = "") }
            is NotesIntent.DraftChanged -> updateState { copy(draft = intent.text) }
            NotesIntent.DismissAddDialog -> updateState { copy(isAddDialogVisible = false) }
            NotesIntent.SaveNote -> saveNote()
        }
    }

    private fun saveNote() {
        val text = currentState.draft.trim()
        if (text.isEmpty()) return

        updateState { copy(isAddDialogVisible = false, draft = "") }
        viewModelScope.launch {
            try {
                notesRepository.addNote(text)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                sendEffect(NotesEffect.ShowSaveError)
            }
        }
    }
}
