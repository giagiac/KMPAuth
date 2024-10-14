package it.hypernext.modacenter.fidelity.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import it.hypernext.modacenter.fidelity.domain.AppSettings
import it.hypernext.modacenter.fidelity.presentation.screen.login.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TextView(text: String, __appString: AppSettings?) {
    val viewModelLogin = koinViewModel<LoginViewModel>()
    val appSettings by viewModelLogin.appSettings

    Text(text = text, modifier = Modifier.fillMaxSize(), )
}