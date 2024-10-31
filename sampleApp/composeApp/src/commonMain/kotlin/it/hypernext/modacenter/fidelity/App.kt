package it.hypernext.modacenter.fidelity

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import it.hypernext.modacenter.fidelity.navigation.Screen
import it.hypernext.modacenter.fidelity.navigation.SetupNavGraph
import it.hypernext.modacenter.fidelity.presentation.screen.login.LoginViewModel
import it.hypernext.modacenter.fidelity.theme.darkScheme
import it.hypernext.modacenter.fidelity.theme.highContrastDarkColorScheme
import it.hypernext.modacenter.fidelity.theme.lightScheme
import it.hypernext.modacenter.fidelity.theme.mediumContrastDarkColorScheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
) {
    val colors = if (false) {
        lightScheme
    } else {
        highContrastDarkColorScheme
    }

    val navController = rememberNavController()

    val viewModelLogin = koinViewModel<LoginViewModel>()
    val appSettings by viewModelLogin.appSettings
    val loadComplete by viewModelLogin.loadComplete

    if (loadComplete) {
        MaterialTheme(colorScheme = colors) {
            var startDestination = Screen.Login.route

            if (appSettings != null) {
                startDestination = Screen.Card.route
            }

            SetupNavGraph(navController, startDestination)
        }
    }
}