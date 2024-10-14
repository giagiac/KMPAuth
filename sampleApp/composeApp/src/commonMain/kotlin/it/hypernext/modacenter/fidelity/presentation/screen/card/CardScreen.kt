package it.hypernext.modacenter.fidelity.presentation.screen.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mmk.kmpnotifier.notification.NotifierManager
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.api.util.NetworkEError
import it.hypernext.modacenter.fidelity.card
import kotlinx.coroutines.delay
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

    var censoredText by remember {
        mutableStateOf<String?>(null)
    }
    var uncensoredText by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf<NetworkEError?>(null)
    }

    val client = remember {
        // InsultCensorClient(createHttpClient(OkHttp.create()))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.card)) },
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
        content = {
//            errorMessage?.let {
//                Text(
//                    text = it.name,
//                    color = Color.Red
//                )
//            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                QRCodeImage(
                    url = "https://www.google.com/",
                    contentScale = ContentScale.Fit,
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(150.dp).padding(top = 32.dp),
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
    )
}