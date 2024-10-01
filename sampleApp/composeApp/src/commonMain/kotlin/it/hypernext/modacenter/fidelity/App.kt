package it.hypernext.modacenter.fidelity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.github.GithubButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.firebase.phone.PhoneAuthContainer
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import com.mmk.kmpauth.uihelper.apple.AppleSignInButtonIconOnly
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleSignInButtonIconOnly
import com.mmk.kmpnotifier.notification.NotifierManager
import dev.gitlive.firebase.auth.FirebaseUser

@Composable
fun App() {
    var myPushNotificationToken by remember { mutableStateOf("") }
    LaunchedEffect(true) {

        println("LaunchedEffectApp is called")
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                myPushNotificationToken = token
                println("onNewToken: $token")
            }
        })
        myPushNotificationToken = NotifierManager.getPushNotifier().getToken() ?: ""
    }

    MaterialTheme {
        Column(
            Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {
            val notifier = remember { NotifierManager.getLocalNotifier() }
            val permissionUtil = remember { NotifierManager.getPermissionUtil() }
            var notificationId by remember { mutableStateOf(0) }
//            Button(onClick = {
//                notificationId = notifier.notify(
//                    title = "Title",
//                    body = "bodyMessage",
//                    payloadData = mapOf(
//                        Notifier.KEY_URL to "https://github.com/mirzemehdi/KMPNotifier/",
//                        "extraKey" to "randomValue"
//                    )
//                )
//            }) {
//                Text("Send Local Notification")
//            }
//            Button(onClick = { notifier.removeAll() }) {
//                Text("Remove all notifications")
//            }
//
//            Button(enabled = notificationId != 0, onClick = {
//                notifier.remove(notificationId)
//                notificationId = 0
//            }) {
//                println("FirebaseToken: $myPushNotificationToken")
//                Text("Remove NotificationID #$notificationId")
//            }
//
//            Text(
//                modifier = Modifier.padding(20.dp),
//                text = "FirebaseToken: $myPushNotificationToken",
//                style = MaterialTheme.typography.body1,
//                textAlign = TextAlign.Start,
//            )


            Button(onClick = {
                permissionUtil.askNotificationPermission {
                    println("Permission is granted")
                }
            }) {
                Text("Ask permission")
            }

            var signedInUserName: String by remember { mutableStateOf("") }
            var errorSignin: String by remember { mutableStateOf("") }
            val onFirebaseResult: (Result<FirebaseUser?>) -> Unit = { result ->
                if (result.isSuccess) {
                    val firebaseUser = result.getOrNull()
                    signedInUserName =
                        firebaseUser?.displayName ?: firebaseUser?.email
                                ?: firebaseUser?.phoneNumber ?: "Null User"
                } else {
                    signedInUserName = ""
                    errorSignin =
                        "Att.ne qualcosa è andato storto, verifica i dati inseriti! ${result.exceptionOrNull()?.message}"
                }

            }
            Text(
                text = signedInUserName,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Start,
            )
            Text(
                text = signedInUserName,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.error
            )

            // ************************** UiHelper Text Buttons *************
            Divider(modifier = Modifier.fillMaxWidth().padding(16.dp))

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

            // ************************** UiHelper Text Buttons *************
            Divider(modifier = Modifier.fillMaxWidth().padding(16.dp))
            AuthUiHelperButtonsAndFirebaseAuth(
                modifier = Modifier.width(IntrinsicSize.Max),
                onFirebaseResult = onFirebaseResult
            )

            //Google Sign-In with Custom Button and authentication without Firebase
            GoogleButtonUiContainer(onGoogleSignInResult = { googleUser ->
                val idToken = googleUser?.idToken // Send this idToken to your backend to verify
                signedInUserName = googleUser?.displayName ?: "Null User"
            }) {
                Button(onClick = { this.onClick() }) { Text("Google Sign-In(Custom Design)") }
            }

            //Apple Sign-In with Custom Button and authentication with Firebase
            AppleButtonUiContainer(onResult = onFirebaseResult) {
                Button(onClick = { this.onClick() }) { Text("Apple Sign-In (Custom Design)") }
            }

            //Github Sign-In with Custom Button and authentication with Firebase
            GithubButtonUiContainer(onResult = onFirebaseResult) {
                Button(onClick = { this.onClick() }) { Text("Github Sign-In (Custom Design)") }
            }


            // ************************** UiHelper Text Buttons *************
            Divider(modifier = Modifier.fillMaxWidth().padding(16.dp))
            AuthUiHelperButtonsAndFirebaseAuth(
                modifier = Modifier.width(IntrinsicSize.Max),
                onFirebaseResult = onFirebaseResult
            )

            //************************** UiHelper IconOnly Buttons *************
            Divider(modifier = Modifier.fillMaxWidth().padding(16.dp))
            IconOnlyButtonsAndFirebaseAuth(
                modifier = Modifier.fillMaxWidth(),
                onFirebaseResult = onFirebaseResult
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

@Composable
fun IconOnlyButtonsAndFirebaseAuth(
    modifier: Modifier = Modifier,
    onFirebaseResult: (Result<FirebaseUser?>) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {

        //Google Sign-In IconOnly Button and authentication with Firebase
        GoogleButtonUiContainerFirebase(onResult = onFirebaseResult) {
            GoogleSignInButtonIconOnly(onClick = { this.onClick() })
        }

        //Apple Sign-In IconOnly Button and authentication with Firebase
        AppleButtonUiContainer(onResult = onFirebaseResult) {
            AppleSignInButtonIconOnly(onClick = { this.onClick() })
        }
    }
}