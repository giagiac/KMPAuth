package it.hypernext.modacenter.fidelity.presentation.screen.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.kmpnotifier.notification.NotifierManager
import dev.gitlive.firebase.auth.FirebaseUser
import it.hypernext.modacenter.fidelity.data.BookDatabase
import it.hypernext.modacenter.fidelity.domain.AppSettings
import it.hypernext.modacenter.fidelity.domain.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val database: BookDatabase
) : ViewModel() {

    companion object {
        const val PROVIDER_PHONE = "PHONE"
        const val PROVIDER_EMAIL = "EMAIL"
    }

    var appSettings: MutableState<AppSettings?> = mutableStateOf(null)
    var loadComplete: MutableState<Boolean> = mutableStateOf(false)

    var loginSucced: MutableState<Boolean> = mutableStateOf(false)

    private var phoneNumber = mutableStateOf("")
    private var email = mutableStateOf("")
    private var provider = mutableStateOf("")

    var errorSignin: MutableState<String?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            database.appSettingsDao()
                .getAppSettings()
//                .onStart {
//                    emit(null)
//
//                }
                .collectLatest {
                    appSettings.value = it
                    loadComplete.value = true
                }
        }
    }

    val onFirebaseResult: (Result<FirebaseUser?>) -> Unit = { result ->
        if (result.isSuccess) {
            val firebaseUser = result.getOrNull()
            if (firebaseUser != null) {

                errorSignin.value = null

                firebaseUser.email?.let { email ->
                    inserUser({
                        loginSucced.value = true
                    }, null, email, PROVIDER_EMAIL)
                }
                firebaseUser.phoneNumber?.let { phone ->
                    inserUser({
                        loginSucced.value = true
                    }, phone, null, PROVIDER_PHONE)
                }
            }
        } else {
            // signedInUserName = ""
            errorSignin.value =
                "Att.ne qualcosa è andato storto, verifica i dati inseriti! ${result.exceptionOrNull()?.message}"
        }
    }

    fun inserUser(
        onSuccess: () -> Unit,
        phoneNumber: String?,
        email: String?,
        provider: String
    ) {
        viewModelScope.launch {
            val token = NotifierManager.getPushNotifier().getToken()

            if (token != null) {
                try {
                    val _id = database.userDao()
                        .insertUser(
                            user = User(
                                phoneNumber = phoneNumber,
                                email = email,
                                idToken = token,
                                privacy = true,
                                provider = provider
                            ),
                        )
                    database.appSettingsDao().insertAppSettings(
                        appSettings = AppSettings(
                            _id = AppSettings.ID,
                            _idUser = _id.toInt(),
                            darkMode = true
                        )
                    )
                    onSuccess()
                } catch (e: Exception) {
                    errorSignin.value = e.message
                }
            }
        }
    }
}