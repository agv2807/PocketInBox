package com.vazdautsan.pocketinbox.composeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vazdautsan.pocketinbox.core.designsystem.theme.PocketInBoxTheme

/** Root of the shared UI, hosted by `MainActivity` on Android and `MainViewController` on iOS. */
@Composable
fun App() {
    PocketInBoxTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("PocketInBox")
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
