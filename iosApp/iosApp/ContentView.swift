import SwiftUI
import UIKit
import ComposeApp

/// Hosts the shared Compose UI from the `composeApp` module.
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        // Insets are handled on the Compose side (Scaffold)
        ComposeView()
            .ignoresSafeArea()
    }
}
