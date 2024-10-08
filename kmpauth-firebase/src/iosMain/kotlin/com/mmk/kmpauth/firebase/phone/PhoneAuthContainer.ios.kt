package com.mmk.kmpauth.firebase.phone

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.PhoneAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError

//On iOS this is needed for some reason, app is recomposed again when navigate to OAuth Screen.
// rememberUpdatedState doesn't solve the problem

@OptIn(ExperimentalForeignApi::class)
@Composable
public actual fun PhoneAuthContainer(
    modifier: Modifier,
//    codeSent: (triggerResend: (Unit)) -> Unit,
//    getVerificationCode: (code: String) -> Unit,
    onResult: (Result<FirebaseUser?>) -> Unit
) {
    val auth = Firebase.auth.ios

    var _verificationCode by remember { mutableStateOf("") }
    var _verificationId by remember { mutableStateOf<String?>(null) }
    var resendToken by remember { mutableStateOf<String?>(null) }
    var errorSend by remember { mutableStateOf<String?>(null) }

    var phoneNumber by remember { mutableStateOf<String?>(null) }

    var phoneNumberEnabled by remember { mutableStateOf(true) }
    var verificationCodeEnabled by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        Column {
            PhoneNumbers(enabled = phoneNumberEnabled, getPhoneNumber = { phone ->
                phoneNumber = phone
            })
            Text(text = errorSend ?: "", color = Color.Red)
            phoneNumber?.let { phone ->
                phoneNumberEnabled = false
                if (_verificationId == null && errorSend == null) {
                    PhoneAuthProvider().ios.verifyPhoneNumber(
                        phoneNumber = phone,
                        null,
                        completion = { token: String?, error: NSError? ->
                            if (error != null) {
                                errorSend =
                                    "Errore di autenticazione : " + error.localizedFailureReason()
                                //return@verifyPhoneNumber
                            } else {
                                // println("Verification id -> $token")
                                println("Token: $token")
                                _verificationId = token
                            }
                        }
                    )
                }
                if (_verificationId != null && errorSend == null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            enabled = verificationCodeEnabled,
                            value = _verificationCode,
                            onValueChange = { _verificationCode = it },
                            label = { Text("Inserisci il codice inviato a $phone") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(enabled = verificationCodeEnabled,
                            onClick = {
                                verificationCodeEnabled = false
                                val authCredential =
                                    PhoneAuthProvider().ios.credentialWithVerificationID(
                                        _verificationId!!,
                                        _verificationCode
                                    )

                                auth.signInWithCredential(authCredential) { result, signInError ->
                                    println("Result: ${result?.user?.phoneNumber}")
                                    println("Error: $signInError")
                                    if (result != null) onResult(Result.success(Firebase.auth.currentUser))
                                    else {
                                        errorSend = signInError?.localizedFailureReason
                                        onResult(Result.failure(IllegalStateException(signInError?.localizedFailureReason)))
                                    }
                                }

                            }) {
                            Text(text = "Conferma")
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}