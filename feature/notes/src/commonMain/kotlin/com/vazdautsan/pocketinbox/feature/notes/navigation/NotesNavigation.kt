package com.vazdautsan.pocketinbox.feature.notes.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vazdautsan.pocketinbox.feature.notes.NotesScreen
import kotlinx.serialization.Serializable

@Serializable
data object NotesRoute

fun NavGraphBuilder.notesScreen() {
    composable<NotesRoute> {
        NotesScreen()
    }
}
