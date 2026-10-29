package com.vazdautsan.pocketinbox.composeapp

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/** Entry point for the iOS app: `MainViewControllerKt.MainViewController()` in Swift. */
@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController = ComposeUIViewController { App() }
