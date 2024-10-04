package it.hypernext.modacenter.fidelity.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import com.mmk.kmpnotifier.notification.NotifierManager
import dev.gitlive.firebase.auth.FirebaseUser
import it.hypernext.modacenter.fidelity.presentation.components.ErrorView
import it.hypernext.modacenter.fidelity.presentation.components.LoadingView
import it.hypernext.modacenter.fidelity.presentation.screen.component.BookView
import it.hypernext.modacenter.fidelity.util.DisplayResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBookSelect: (Int) -> Unit,
    onCreateClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val viewModel = koinViewModel<HomeViewModel>()
    val books by viewModel.books
    val sortedByFavorite by viewModel.sortedByFavorite.collectAsStateWithLifecycle()

    var myPushNotificationToken by remember { mutableStateOf("") }
    LaunchedEffect(true) {

        println("LaunchedEffectApp is called")
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                myPushNotificationToken = token
                println("onNewToken: $token")
            }
        })
        myPushNotificationToken = NotifierManager.getPushNotifier().getToken() ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Book Library") },
                actions = {
                    IconButton(
                        onClick = {
                            if (books.isSuccess() && books.getSuccessData().size >= 2) {
                                viewModel.toggleSortByFavorite()
                                scope.launch {
                                    delay(100)
                                    listState.animateScrollToItem(0)
                                }
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.alpha(
                                if (sortedByFavorite) 1f else 0.38f
                            ),
                            imageVector = Icons.Default.Star,
                            contentDescription = "Sorting Icon",
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Icon"
                )
            }
        }
    ) {
        books.DisplayResult(
            onLoading = { LoadingView() },
            onError = { ErrorView(it) },
            onSuccess = { data ->
                if (data.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .padding(
                                top = it.calculateTopPadding(),
                                bottom = it.calculateBottomPadding()
                            ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = data,
                            key = { it._id }
                        ) {
                            BookView(
                                book = it,
                                onClick = { onBookSelect(it._id) }
                            )
                        }
                    }
                } else {
                    ErrorView()
                }
            }
        )
    }
}

@Composable
fun AuthUiHelperButtonsAndFirebaseAuth(
    modifier: Modifier = Modifier,
    onFirebaseResult: (Result<FirebaseUser?>) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        //Google Sign-In Button and authentication with Firebase
        GoogleButtonUiContainerFirebase(onResult = onFirebaseResult) {
            GoogleSignInButton(
                modifier = Modifier.fillMaxWidth().height(44.dp),
                fontSize = 19.sp
            ) { this.onClick() }
        }

        //Apple Sign-In Button and authentication with Firebase
        AppleButtonUiContainer(onResult = onFirebaseResult) {
            AppleSignInButton(modifier = Modifier.fillMaxWidth().height(44.dp)) { this.onClick() }
        }

    }
}