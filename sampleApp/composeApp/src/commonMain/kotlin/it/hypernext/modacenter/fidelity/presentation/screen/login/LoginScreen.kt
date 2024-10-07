package it.hypernext.modacenter.fidelity.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.mmk.kmpnotifier.notification.NotifierManager
import dev.gitlive.firebase.auth.FirebaseUser
import it.hypernext.modacenter.fidelity.presentation.screen.login.LoginViewModel.Companion.PROVIDER_EMAIL
import it.hypernext.modacenter.fidelity.presentation.screen.login.LoginViewModel.Companion.PROVIDER_PHONE
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val viewModelAppSettings = koinViewModel<LoginViewModel>()

    LaunchedEffect(true) {
        println("LaunchedEffectApp is called")
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                viewModelAppSettings.idToken.value = token
                println("onNewToken: $token")
            }
        })
        viewModelAppSettings.idToken.value = NotifierManager.getPushNotifier().getToken() ?: ""
    }

    // var signedInUserName: String? by remember { mutableStateOf(null) }

    var errorSignin: String? by remember { mutableStateOf(null) }

    val onFirebaseResult: (Result<FirebaseUser?>) -> Unit = { result ->
        if (result.isSuccess) {
            val firebaseUser = result.getOrNull()
            // signedInUserName = firebaseUser?.displayName ?: firebaseUser?.email ?: firebaseUser?.phoneNumber ?: "Null User"
            if (firebaseUser != null) {

                errorSignin = null

                firebaseUser.email?.let {
                    viewModelAppSettings.email.value = it
                    viewModelAppSettings.inserUser(
                        onSuccess = {
                            println("onSuccess")
                            onLoginSuccess()
                            viewModelAppSettings.provider.value = PROVIDER_EMAIL
                            // onBookSelect(0)
                        },
                        onError = {}
                    )
                }
                firebaseUser.phoneNumber?.let {
                    viewModelAppSettings.phoneNumber.value = it
                    viewModelAppSettings.inserUser(
                        onSuccess = {
                            println("onSuccess")
                            onLoginSuccess()
                            viewModelAppSettings.provider.value = PROVIDER_PHONE
                            // onBookSelect(0)
                        },
                        onError = {}
                    )
                }
            }
        } else {
            // signedInUserName = ""
            errorSignin =
                "Att.ne qualcosa è andato storto, verifica i dati inseriti! ${result.exceptionOrNull()?.message}"
        }
    }

    Column(
        // modifier = Modifier.offset(y = (-32).dp), // Sposta verso l'alto di 16dp
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 16.dp),
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
//        signedInUserName?.let {
//            Row {
//                Text(
//                    text = it,
//                    style = MaterialTheme.typography.bodyLarge,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
        errorSignin?.let {
            Row {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        Row {
            // ************************** UiHelper Text Buttons *************
            AuthUiHelperButtonsAndFirebaseAuth(
                modifier = Modifier.width(IntrinsicSize.Max),
                onFirebaseResult = onFirebaseResult
            )
        }
        Row {
            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp, top = 16.dp))
        }
        Row {
            // ************************** UiHelper Text Buttons *************
            PhoneAuthContainer(
                modifier = Modifier.fillMaxWidth(),
                codeSent = { token ->
                    // TODO
                    print(token)
                },
                getVerificationCode = fun(code: String) {
                    // TODO
                    print(code)
                },
                onResult = onFirebaseResult
            )

        }
    }
}

@Composable
fun AuthUiHelperButtonsAndFirebaseAuth(
    modifier: Modifier = Modifier,
    onFirebaseResult: (Result<FirebaseUser?>) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        //Google Sign-In Button and authentication with Firebase
        GoogleButtonUiContainerFirebase(onResult = onFirebaseResult) {
            GoogleSignInButton(
                modifier = Modifier.fillMaxWidth().height(44.dp),
                fontSize = 19.sp
            ) { this.onClick() }
        }

        //Apple Sign-In Button and authentication with Firebase
        AppleButtonUiContainer(onResult = onFirebaseResult) {
            AppleSignInButton(modifier = Modifier.fillMaxWidth().height(44.dp)) { this.onClick() }
        }

    }
}