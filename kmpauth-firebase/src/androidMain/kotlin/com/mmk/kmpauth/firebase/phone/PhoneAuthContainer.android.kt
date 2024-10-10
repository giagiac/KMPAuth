package com.mmk.kmpauth.firebase.phone

import android.os.Parcelable
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.mmk.kmpauth.core.KMPAuthInternalApi
import com.mmk.kmpauth.core.getActivity
import dev.gitlive.firebase.auth.auth
import java.util.concurrent.TimeUnit

@OptIn(KMPAuthInternalApi::class)
@Composable
public actual fun PhoneAuthContainer(
    modifier: Modifier,
//    codeSent: (triggerResend: (Unit)) -> Unit,
//    getVerificationCode: (code: String) -> Unit,
    onResult: (Result<dev.gitlive.firebase.auth.FirebaseUser?>) -> Unit
) {
    val activity = LocalContext.current.getActivity()

    val auth = FirebaseAuth.getInstance()

    var _verificationCode by remember { mutableStateOf("") }
    var _verificationId by remember { mutableStateOf<String?>(null) }
    var resendToken by remember { mutableStateOf<Parcelable?>(null) }
    var errorSend by remember { mutableStateOf<String?>(null) }

    var phoneNumber by remember { mutableStateOf<String?>(null) }

    var phoneNumberEnabled by remember { mutableStateOf(true) }
    var verificationCodeEnabled by remember { mutableStateOf(false) }

//    val coroutineScope = MainScope()
//
//    val uiContainerScope = remember {
//        object : UiContainerScope {
//            override fun onClick() {
//                coroutineScope.launch {
//                    // val result = onClickSignIn(oAuthProvider)
//                    // mOnResult?.invoke(result)
//                    println("Clicked")
//                }
//            }
//        }
//    }

    Box(modifier = modifier) {
        Column {
            PhoneNumbers(enabled = phoneNumberEnabled, getPhoneNumber = { phone ->
                phoneNumber = phone
            })
            Text(text = errorSend ?: "", color = Color.Red)
            phoneNumber?.let { phone ->
                phoneNumberEnabled = false
                if (_verificationId == null && errorSend == null) {
                    val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity!!) // Make sure your MainActivity inherits ComponentActivity
                        .setCallbacks(object :
                            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                // Auto-retrieval or instant validation is handled here
                                print(credential)
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                Log.e("MainActivity", "Verification failed", e)
                                errorSend = e.message
                            }

                            override fun onCodeSent(
                                verificationId: String,
                                token: PhoneAuthProvider.ForceResendingToken
                            ) {
                                Log.d("MainActivity", "Code sent: $verificationId")
                                resendToken = token
                                _verificationId = verificationId
                                verificationCodeEnabled = true
                            }
                        }).build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
                if (_verificationId != null && errorSend == null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = _verificationCode,
                            onValueChange = { _verificationCode = it },
                            label = {
                                Text("Inserisci il codice inviato a $phone")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = verificationCodeEnabled,
                            textStyle = TextStyle(fontSize = 24.sp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(enabled = verificationCodeEnabled, onClick = {
                            verificationCodeEnabled = false
                            val credential =
                                PhoneAuthProvider.getCredential(
                                    _verificationId!!,
                                    _verificationCode
                                )
                            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = task.result?.user
                                    if (user != null) {
                                        println("Authentication succeeded for user: ${user.phoneNumber}")
                                        onResult.invoke(Result.success(dev.gitlive.firebase.Firebase.auth.currentUser))
                                    } else {
                                        throw Exception("User is null")
                                    }
                                } else {
                                    task.exception ?: throw Exception("Authentication failed")
                                }
                            }

                        }) {
                            Text(text = "Conferma")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}