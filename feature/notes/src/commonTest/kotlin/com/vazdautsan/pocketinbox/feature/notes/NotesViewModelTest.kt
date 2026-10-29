package com.vazdautsan.pocketinbox.feature.notes

import app.cash.turbine.test
import com.vazdautsan.pocketinbox.core.domain.model.Note
import com.vazdautsan.pocketinbox.core.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val repository = FakeNotesRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadingUntilRepositoryEmits() = runTest {
        val viewModel = NotesViewModel(repository)
        assertTrue(viewModel.state.value.isLoading)

        val notes = listOf(Note(id = 1, text = "Hello", createdAt = Instant.fromEpochMilliseconds(0)))
        repository.notes.emit(notes)

        assertFalse(viewModel.state.value.isLoading)
        assertEquals(notes, viewModel.state.value.notes)
    }

    @Test
    fun saveAddsTrimmedNoteAndClosesDialog() = runTest {
        val viewModel = NotesViewModel(repository)

        viewModel.onIntent(NotesIntent.AddNoteClicked)
        assertTrue(viewModel.state.value.isAddDialogVisible)
        viewModel.onIntent(NotesIntent.DraftChanged("  Buy milk  "))
        viewModel.onIntent(NotesIntent.SaveNote)

        assertEquals(listOf("Buy milk"), repository.added)
        assertFalse(viewModel.state.value.isAddDialogVisible)
        assertEquals("", viewModel.state.value.draft)
    }

    @Test
    fun blankDraftIsNotSaved() = runTest {
        val viewModel = NotesViewModel(repository)

        viewModel.onIntent(NotesIntent.AddNoteClicked)
        viewModel.onIntent(NotesIntent.DraftChanged("   "))
        viewModel.onIntent(NotesIntent.SaveNote)

        assertEquals(emptyList(), repository.added)
        assertTrue(viewModel.state.value.isAddDialogVisible)
    }

    @Test
    fun dismissClosesDialogAndReopenClearsDraft() = runTest {
        val viewModel = NotesViewModel(repository)

        viewModel.onIntent(NotesIntent.AddNoteClicked)
        viewModel.onIntent(NotesIntent.DraftChanged("draft"))
        viewModel.onIntent(NotesIntent.DismissAddDialog)
        assertFalse(viewModel.state.value.isAddDialogVisible)

        viewModel.onIntent(NotesIntent.AddNoteClicked)
        assertEquals("", viewModel.state.value.draft)
    }

    @Test
    fun saveFailureShowsError() = runTest {
        repository.failOnAdd = true
        val viewModel = NotesViewModel(repository)

        viewModel.effects.test {
            viewModel.onIntent(NotesIntent.DraftChanged("Note"))
            viewModel.onIntent(NotesIntent.SaveNote)

            assertEquals(NotesEffect.ShowSaveError, awaitItem())
        }
    }

    private class FakeNotesRepository : NotesRepository {
        val notes = MutableSharedFlow<List<Note>>()
        val added = mutableListOf<String>()
        var failOnAdd = false

        override fun observeNotes(): Flow<List<Note>> = notes

        override suspend fun addNote(text: String) {
            if (failOnAdd) error("Disk is full")
            added += text
        }
    }
}
