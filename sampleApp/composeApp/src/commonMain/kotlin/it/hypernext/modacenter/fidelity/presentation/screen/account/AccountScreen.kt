package it.hypernext.modacenter.fidelity.presentation.screen.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    bottomBar: @Composable () -> Unit,
    onLogout: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val viewModel = koinViewModel<AccountViewModel>()
    val books by viewModel.books
    val sortedByFavorite by viewModel.sortedByFavorite.collectAsStateWithLifecycle()

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.account),
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                actions = {
//                    IconButton(
//                        onClick = {
//                            if (books.isSuccess() && books.getSuccessData().size >= 2) {
//                                viewModel.toggleSortByFavorite()
//                                scope.launch {
//                                    delay(100)
//                                    listState.animateScrollToItem(0)
//                                }
//                            }
//                        }
//                    ) {
//                        Icon(
//                            modifier = Modifier.alpha(
//                                if (sortedByFavorite) 1f else 0.38f
//                            ),
//                            imageVector = Icons.Default.Star,
//                            contentDescription = "Sorting Icon",
//                        )
//                    }
                }
            )
        },
        bottomBar = bottomBar,
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                viewModel.user.value?.let { user ->
                    Row {
                        UserCard(user = user)
                    }
                    Row {
                        Button(onClick = {
                            viewModel.logout()
                            onLogout()
                        }, content = { Text("Logout") })
                    }
                }
                viewModel.userDetail.value.let { userDetail ->
                    userDetail.DisplayResult(
                        onLoading = { LoadingView() },
                        onError = { error -> ErrorView(error) },
                        onSuccess = { data ->
                            if (data.listScores.isNotEmpty()) {
                                Row {
                                    UserDetailCard(userDetail = data)
                                }
                            }
                        })
                }
            }
//            errorMessage?.let {
//                Text(
//                    text = it.name,
//                    color = Color.Red
//                )
//            }
        }
    )
}