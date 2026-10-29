package com.vazdautsan.pocketinbox.feature.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vazdautsan.pocketinbox.core.common.mvi.CollectEffects
import com.vazdautsan.pocketinbox.core.designsystem.theme.PocketInBoxTheme
import com.vazdautsan.pocketinbox.core.domain.model.Note
import com.vazdautsan.pocketinbox.feature.notes.resources.Res
import com.vazdautsan.pocketinbox.feature.notes.resources.action_cancel
import com.vazdautsan.pocketinbox.feature.notes.resources.action_save
import com.vazdautsan.pocketinbox.feature.notes.resources.ic_add
import com.vazdautsan.pocketinbox.feature.notes.resources.notes_add
import com.vazdautsan.pocketinbox.feature.notes.resources.notes_add_placeholder
import com.vazdautsan.pocketinbox.feature.notes.resources.notes_empty_hint
import com.vazdautsan.pocketinbox.feature.notes.resources.notes_empty_title
import com.vazdautsan.pocketinbox.feature.notes.resources.notes_save_error
import com.vazdautsan.pocketinbox.feature.notes.resources.notes_title
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant

@Composable
internal fun NotesScreen(viewModel: NotesViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            NotesEffect.ShowSaveError -> snackbarHostState.showSnackbar(getString(Res.string.notes_save_error))
        }
    }

    NotesContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotesContent(
    state: NotesState,
    snackbarHostState: SnackbarHostState,
    onIntent: (NotesIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.notes_title)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onIntent(NotesIntent.AddNoteClicked) }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.notes_add),
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        when {
            state.isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            state.notes.isEmpty() -> EmptyNotes(Modifier.padding(innerPadding))

            else -> NotesList(notes = state.notes, innerPadding = innerPadding)
        }
    }

    if (state.isAddDialogVisible) {
        AddNoteDialog(
            draft = state.draft,
            onDraftChange = { onIntent(NotesIntent.DraftChanged(it)) },
            onSave = { onIntent(NotesIntent.SaveNote) },
            onDismiss = { onIntent(NotesIntent.DismissAddDialog) },
        )
    }
}

@Composable
private fun EmptyNotes(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.notes_empty_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.notes_empty_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun NotesList(notes: List<Note>, innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = innerPadding.calculateTopPadding() + 8.dp,
            // Keeps the last card above the FAB
            bottom = innerPadding.calculateBottomPadding() + 88.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(notes, key = { it.id }) { note ->
            NoteCard(note)
        }
    }
}

@Composable
private fun NoteCard(note: Note) {
    val date = remember(note.createdAt) { formatNoteDate(note.createdAt) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.text, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun AddNoteDialog(
    draft: String,
    onDraftChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.notes_add)) },
        text = {
            OutlinedTextField(
                value = draft,
                onValueChange = onDraftChange,
                placeholder = { Text(stringResource(Res.string.notes_add_placeholder)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            )
        },
        confirmButton = {
            TextButton(onClick = onSave, enabled = draft.isNotBlank()) {
                Text(stringResource(Res.string.action_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.action_cancel))
            }
        },
    )
}

@Preview
@Composable
private fun NotesContentEmptyPreview() {
    PocketInBoxTheme {
        NotesContent(
            state = NotesState(isLoading = false),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun NotesContentPreview() {
    PocketInBoxTheme {
        NotesContent(
            state = NotesState(
                isLoading = false,
                notes = listOf(
                    Note(id = 2, text = "Buy milk", createdAt = Instant.fromEpochMilliseconds(1_761_760_000_000)),
                    Note(id = 1, text = "Call mom", createdAt = Instant.fromEpochMilliseconds(1_761_700_000_000)),
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {},
        )
    }
}
