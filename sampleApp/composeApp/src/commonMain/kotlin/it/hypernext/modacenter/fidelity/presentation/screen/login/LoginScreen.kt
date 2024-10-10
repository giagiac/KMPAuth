package it.hypernext.modacenter.fidelity.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.firebase.phone.PhoneAuthContainer
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val viewModelAppSettings = koinViewModel<LoginViewModel>()

    LaunchedEffect(true) {

        println("LaunchedEffectApp is called")

    }

    if (viewModelAppSettings.loginSucced.value) {
        onLoginSuccess()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row {
            Text(
                text = "Login",
                style = MaterialTheme.typography.titleLarge,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                fontSize = 100.sp
            )
        }
        viewModelAppSettings.errorSignin.let { error ->
            Row {
                Text(
                    text = error.value ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            GoogleButtonUiContainerFirebase(onResult = viewModelAppSettings.onFirebaseResult) {
                GoogleSignInButton(
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    fontSize = 19.sp
                ) { this.onClick() }
            }
        }
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            AppleButtonUiContainer(onResult = viewModelAppSettings.onFirebaseResult) {
                AppleSignInButton(
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) { this.onClick() }
            }
        }
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            HorizontalDivider()
        }
        Row {
            // ************************** UiHelper Text Buttons *************
            PhoneAuthContainer(
                modifier = Modifier.fillMaxWidth(),
                onResult = viewModelAppSettings.onFirebaseResult
            )

        }
    }
}
