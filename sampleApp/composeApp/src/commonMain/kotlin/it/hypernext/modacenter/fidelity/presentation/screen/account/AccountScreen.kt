package it.hypernext.modacenter.fidelity.presentation.screen.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mmk.kmpnotifier.notification.NotifierManager
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.account
import it.hypernext.modacenter.fidelity.presentation.components.ErrorView
import it.hypernext.modacenter.fidelity.presentation.components.LoadingView
import it.hypernext.modacenter.fidelity.presentation.screen.card.UserCard
import it.hypernext.modacenter.fidelity.presentation.screen.card.UserDetailCard
import it.hypernext.modacenter.fidelity.util.DisplayResult
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    bottomBar: @Composable () -> Unit, onLogout: () -> Unit
) {
    val viewModel = koinViewModel<AccountViewModel>()
    val userDetail by viewModel.userDetail

    var myPushNotificationToken by remember { mutableStateOf("") }
    LaunchedEffect(true) {

        println("LaunchedEffectApp is called")
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                myPushNotificationToken = token
                println("onNewToken: $token")
                viewModel.sendData(token)
            }
        })
        myPushNotificationToken = NotifierManager.getPushNotifier().getToken() ?: ""
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(Res.string.account),
                style = MaterialTheme.typography.headlineLarge
            )
        })
    }, bottomBar = bottomBar, content = { it ->
        Scaffold(
            modifier = Modifier.padding(it).padding(start = 8.dp, end = 8.dp),
            topBar = {
                viewModel.user.value?.let { user ->
                    Column {
                        Row {
                            UserCard(user = user)
                        }
                        Row(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                                .padding(top = 8.dp)
                        ) {
                            Button(onClick = {
                                viewModel.logout()
                                onLogout()
                            }, content = { Text("Logout") })
                        }
                    }
                }
            }, content = {
                userDetail.DisplayResult(
                    onLoading = { LoadingView() },
                    onError = { ErrorView(it) },
                    onSuccess = { data ->
                        Row {
                            UserDetailCard(userDetail = data)
                        }
                    })
            })
    })
}