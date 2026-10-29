package com.vazdautsan.pocketinbox.feature.notes

import com.vazdautsan.pocketinbox.core.domain.model.Note

internal data class NotesState(
    val isLoading: Boolean = true,
    val notes: List<Note> = emptyList(),
    val isAddDialogVisible: Boolean = false,
    val draft: String = "",
)

internal sealed interface NotesIntent {
    data object AddNoteClicked : NotesIntent
    data class DraftChanged(val text: String) : NotesIntent
    data object SaveNote : NotesIntent
    data object DismissAddDialog : NotesIntent
}

internal sealed interface NotesEffect {
    data object ShowSaveError : NotesEffect
}
