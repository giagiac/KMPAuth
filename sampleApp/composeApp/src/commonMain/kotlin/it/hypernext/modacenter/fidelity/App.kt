package it.hypernext.modacenter.fidelity

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import it.hypernext.modacenter.fidelity.navigation.Screen
import it.hypernext.modacenter.fidelity.navigation.SetupNavGraph
import it.hypernext.modacenter.fidelity.presentation.screen.login.LoginViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    val viewModelLogin = koinViewModel<LoginViewModel>()
    val appSettings by viewModelLogin.appSettings
    val loadComplete by viewModelLogin.loadComplete

    if (loadComplete) {
        MaterialTheme {
            var startDestination = Screen.Login.route

            if (appSettings != null) {
                startDestination = Screen.Home.route
            }

            SetupNavGraph(navController, startDestination)
        }
    }
}