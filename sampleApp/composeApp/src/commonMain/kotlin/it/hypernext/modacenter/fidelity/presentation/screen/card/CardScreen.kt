package it.hypernext.modacenter.fidelity.presentation.screen.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mmk.kmpnotifier.notification.NotifierManager
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.card
import it.hypernext.modacenter.fidelity.presentation.components.ErrorView
import it.hypernext.modacenter.fidelity.presentation.components.LoadingView
import it.hypernext.modacenter.fidelity.presentation.screen.component.PointView
import it.hypernext.modacenter.fidelity.score
import it.hypernext.modacenter.fidelity.util.DisplayResult
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.QRCodeImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(
    bottomBar: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val viewModel = koinViewModel<CardViewModel>()
    val userDetail by viewModel.userDetail

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

    val client = remember {
        // InsultCensorClient(createHttpClient(OkHttp.create()))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.card),
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO: implement sorting
                        }
                    ) {

                    }
                }
            )
        },
        bottomBar = bottomBar,
        content = { it ->
//            Box(modifier = Modifier
//                .padding(it)
//                .padding(start = 8.dp, end = 8.dp)
//
//                .verticalScroll(rememberScrollState())
//                .fillMaxSize(),
//            ){
//
//            }
            Scaffold(
                modifier = Modifier.padding(it).padding(start = 8.dp, end = 8.dp),
                topBar = {
                    viewModel.user.value?.let {
                        Row {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                QRCodeImage(
                                    url = it.uid,
                                    contentScale = ContentScale.Fit,
                                    contentDescription = it.uid,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .size(150.dp).padding(16.dp),
                                    onSuccess = { qrImage ->

                                    },
                                    onFailure = {
                                        scope.launch {
                                            // TODO: handle error
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                content = {
                    userDetail.DisplayResult(
                        onLoading = { LoadingView() },
                        onError = { ErrorView(it) },
                        onSuccess = { data ->
                            if (data.listScores.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .padding(
                                            top = it.calculateTopPadding(),
                                            bottom = it.calculateBottomPadding()
                                        )
                                ) {
                                    Row {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            text = data.score,
                                            style = MaterialTheme.typography.displayMedium
                                        )
                                    }
                                    Row {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            text = stringResource(Res.string.score),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                    Row {
                                        LazyColumn(
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            items(
                                                items = data.listScores,
                                                key = { it.dataScan }
                                            ) {
                                                PointView(
                                                    score = it,
                                                    onClick = {
                                                        //onBookSelect(it._id)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }


                            } else {
                                ErrorView()
                            }
                        }
                    )
                },
                bottomBar = {
                    viewModel.error.value?.let { error ->
                        Row {
                            Text(
                                text = error,
                                color = Color.Red
                            )
                        }
                    }
                }
            )

        })
}