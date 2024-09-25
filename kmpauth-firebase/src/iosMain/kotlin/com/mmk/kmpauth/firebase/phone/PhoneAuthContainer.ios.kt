package com.mmk.kmpauth.firebase.phone

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.core.UiContainerScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.OAuthProvider
import dev.gitlive.firebase.auth.PhoneAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

//On iOS this is needed for some reason, app is recomposed again when navigate to OAuth Screen.
// rememberUpdatedState doesn't solve the problem

/**
 * OAuth Ui Container Composable that handles all sign-in functionality for given provider.
 * Child of this Composable can be any view or Composable function.
 * You need to call [UiContainerScope.onClick] function on your child view's click function.
 *
 * [onResult] callback will return [Result] with [FirebaseUser] type.
 * @param phoneAuthProvider [OAuthProvider] class object.
 *
 * Example Usage:
 * ```
 *
 * OAuthContainer(onResult = onFirebaseResult) {
 *     Button(onClick = { this.onClick() }) { Text("Github Sign-In (Custom Design)") }
 * }
 * val oAuthProvider = OAuthProvider(provider = "github.com")
 * OAuthContainer(modifier = modifier, oAuthProvider = oAuthProvider,onResult = onFirebaseResult){
 *  Button(onClick = { this.onClick() }) { Text("Github Sign-In (Custom Design)") }
 * }
 *
 * ```
 *
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
public actual fun PhoneAuthContainer(
    modifier: Modifier,
    phoneNumber: String,
    codeSent: (triggerResend: (Unit)) -> Unit,
    getVerificationCode: (code: String) -> Unit,
    content: @Composable UiContainerScope.() -> Unit,
    onResult: (Result<FirebaseUser?>) -> Unit,
) {
//    val oAuthProvider = OAuthProvider(provider = "apple.com")
//    val updatedOnResultFunc by rememberUpdatedState(onResult)
//    mOnResult = updatedOnResultFunc
    val coroutineScope = MainScope()

//    FIRAuth.initialize()
//    val app = Firebase.auth.ios.app()

    var _verificationCode = remember { mutableStateOf("") }
    var _verificationId = remember { mutableStateOf("") }

    val uiContainerScope = remember {
        object : UiContainerScope {
            override fun onClick() {
                coroutineScope.launch {
                    // val result = onClickSignIn(oAuthProvider)
                    // mOnResult?.invoke(result)
                    println("Clicked")
                    // val p = FIRPhoneAuthCredential.initialize()
                    // var credential = PhoneAuthCredential(FIRPhoneAuthCredential())

                    try {
                        PhoneAuthProvider().ios.verifyPhoneNumber(phoneNumber = phoneNumber, null) { verificationId, token ->
                            println("Verification id -> $verificationId")
                            println("Token: $token")

                            getVerificationCode(verificationId!!.subSequence(0,5).toString())

                            _verificationId.value = verificationId
                        }
                    }catch (ex: Exception){
                        print(ex.message)
                    }

                }
            }
        }
    }
    Box(modifier = modifier) {
        Column {
            TextField(modifier = Modifier.fillMaxWidth(), value = _verificationCode.value, onValueChange = {
                _verificationCode.value = it
            })
            Button(onClick = {
                val authCredential = PhoneAuthProvider().ios.credentialWithVerificationID(_verificationId.value, _verificationCode.value)

                val auth = Firebase.auth.ios

                auth.signInWithCredential(authCredential) { result, signInError ->
                    println("Result: ${result?.user?.phoneNumber}")
                    println("Error: $signInError")
                    if (result != null) Result.success(Firebase.auth.currentUser)
                    else Result.failure(IllegalStateException(signInError?.localizedFailureReason))
                }
            },content= { Text("Click to send")})
            uiContainerScope.content()
        }
    }
}