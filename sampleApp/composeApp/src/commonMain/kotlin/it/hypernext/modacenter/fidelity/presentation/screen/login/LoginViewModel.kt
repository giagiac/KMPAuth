package it.hypernext.modacenter.fidelity.presentation.screen.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.hypernext.modacenter.fidelity.data.BookDatabase
import it.hypernext.modacenter.fidelity.domain.AppSettings
import it.hypernext.modacenter.fidelity.domain.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val database: BookDatabase
) : ViewModel() {

    companion object {
        val PROVIDER_PHONE = "PHONE"
        val PROVIDER_EMAIL = "EMAIL"
    }

    var appSettings: MutableState<AppSettings?> = mutableStateOf(null)
    var loadComplete: MutableState<Boolean> = mutableStateOf(false)

    var phoneNumber = mutableStateOf("")
    var email = mutableStateOf("")
    var idToken = mutableStateOf("")
    var provider = mutableStateOf("")

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

    fun inserUser(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val _id = database.userDao()
                    .insertUser(
                        user = User(
                            phoneNumber = phoneNumber.value,
                            email = email.value,
                            idToken = idToken.value,
                            privacy = true,
                            provider = provider.value
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
                onError(e.toString())
            }
        }
    }
}