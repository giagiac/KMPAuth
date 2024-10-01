package com.mmk.kmpauth.firebase.phone

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.gitlive.firebase.auth.FirebaseUser

@Composable
public expect fun PhoneAuthContainer(
    modifier: Modifier = Modifier,
    // onResult: (Result<FirebaseUser?>) -> Unit,
    codeSent: (triggerResend: (Unit)) -> Unit,
    getVerificationCode: (code: String) -> Unit,
    onResult: (Result<FirebaseUser?>) -> Unit
)