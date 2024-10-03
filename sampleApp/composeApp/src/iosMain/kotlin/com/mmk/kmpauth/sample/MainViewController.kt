package it.hypernext.modacenter.fidelity

import androidx.compose.ui.window.ComposeUIViewController
import it.hypernext.modacenter.fidelity.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }


