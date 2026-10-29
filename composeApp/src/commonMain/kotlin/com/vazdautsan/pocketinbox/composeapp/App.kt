package com.vazdautsan.pocketinbox.composeapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vazdautsan.pocketinbox.core.designsystem.theme.PocketInBoxTheme
import com.vazdautsan.pocketinbox.feature.notes.navigation.NotesRoute
import com.vazdautsan.pocketinbox.feature.notes.navigation.notesScreen

/** Root of the shared UI, hosted by `MainActivity` on Android and `MainViewController` on iOS. */
@Composable
fun App() {
    PocketInBoxTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = NotesRoute) {
            notesScreen()
        }
    }
}
