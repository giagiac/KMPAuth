package com.mmk.kmpauth.firebase.phone

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.mmk.kmpauth.core.KMPAuthInternalApi
import com.mmk.kmpauth.core.UiContainerScope
import com.mmk.kmpauth.core.getActivity
import java.util.concurrent.TimeUnit

@OptIn(KMPAuthInternalApi::class)
@Composable
public actual fun PhoneAuthContainer(
    modifier: Modifier,
    phoneNumber: String,
    codeSent: (triggerResend: (Unit)) -> Unit,
    getVerificationCode: (code: String) -> Unit,
    content: @Composable UiContainerScope.() -> Unit,
    onResult: (Result<dev.gitlive.firebase.auth.FirebaseUser?>) -> Unit,
) {
    val activity = LocalContext.current.getActivity()

    var verificationCode by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }

    val uiContainerScope = remember {
        object : UiContainerScope {
            override fun onClick() {
                sendVerificationCode(activity!!,
                    phoneNumber,
                    FirebaseAuth.getInstance(),
                    { id, token ->
                        verificationId = id
//                    resendToken = token
                    },
                    { exception ->
                        println("Error : ${exception.message}")
                    })
//                activity?.let {
//                    val phoneVerificationProvider =
//                        VerificationProvider(activity, 60, TimeUnit.SECONDS)
//                    phoneVerificationProvider.codeSent { codeSent(it) }
//                    val coroutineScope = MainScope()
//                    coroutineScope.launch {
//                        PhoneAuthProvider().verifyPhoneNumber(
//                            phoneNumber, verificationProvider = phoneVerificationProvider
//                        )
//                        val verificationCode = phoneVerificationProvider.getVerificationCode()
//                        getVerificationCode(verificationCode)
//                    }
//                }
            }
        }
    }

    Box(modifier = modifier) {
        Column {
            uiContainerScope.content()
            verificationId?.let {
                Text(text = "Enter the verification code sent to $phoneNumber")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    label = { Text("Enter verification code") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    verifyCode(
                        verificationCode,
                        verificationId!!,
                        FirebaseAuth.getInstance(),
                        { user ->
                            println("Authentication succeeded for user: ${user.phoneNumber}")
                        },
                        { exception ->
                            println("Authentication failed: ${exception.message}")
                        })
                }) {
                    Text(text = verificationId + verificationCode)
                }
            }
        }
    }
}

private fun sendVerificationCode(
    activity: Activity,
    phoneNumber: String,
    auth: FirebaseAuth,
    onCodeSent: (String, com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken) -> Unit,
    onVerificationFailed: (FirebaseException) -> Unit
) {
    val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity) // Make sure your MainActivity inherits ComponentActivity
        .setCallbacks(object :
            com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-retrieval or instant validation is handled here
                print(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("MainActivity", "Verification failed", e)
                onVerificationFailed(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("MainActivity", "Code sent: $verificationId")
                onCodeSent(verificationId, token)
            }
        }).build()
    com.google.firebase.auth.PhoneAuthProvider.verifyPhoneNumber(options)
}

private fun verifyCode(
    code: String,
    verificationId: String,
    auth: FirebaseAuth,
    onSuccess: (FirebaseUser) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val credential = com.google.firebase.auth.PhoneAuthProvider.getCredential(verificationId, code)
    auth.signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val user = task.result?.user
            if (user != null) {
                onSuccess(user)
            } else {
                onFailure(Exception("User is null"))
            }
        } else {
            onFailure(task.exception ?: Exception("Authentication failed"))
        }
    }
}
