package it.hypernext.modacenter.fidelity.presentation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.kmpnotifier.notification.NotifierManager
import it.hypernext.modacenter.fidelity.api.InsultCensorClient
import it.hypernext.modacenter.fidelity.api.PushNotificationClient
import it.hypernext.modacenter.fidelity.data.BookDatabase
import it.hypernext.modacenter.fidelity.domain.Book
import it.hypernext.modacenter.fidelity.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val database: BookDatabase,
    private val censorClient: InsultCensorClient,
    private val pushNotificationClient: PushNotificationClient
) : ViewModel() {
    private var _sortedByFavorite = MutableStateFlow(false)
    val sortedByFavorite: StateFlow<Boolean> = _sortedByFavorite

    private var _books: MutableState<RequestState<List<Book>>> =
        mutableStateOf(RequestState.Loading)
    val books: State<RequestState<List<Book>>> = _books

    init {
        viewModelScope.launch {
            println(censorClient.censorWords("Fuck"))

            // val idToken = database.userDao().getUserById(1).idToken

            NotifierManager.getPushNotifier().getToken()
                ?.let { pushNotificationClient.sendData(it) }

            _sortedByFavorite.collectLatest { favorite ->
                if (favorite) {
                    database.bookDao()
                        .readAllBooksSortByFavorite()
                        .collectLatest { sortedBooks ->
                            _books.value = RequestState.Success(
                                data = sortedBooks.sortedBy { !it.isFavorite }
                            )
                        }
                } else {
                    database.bookDao()
                        .readAllBooks()
                        .collectLatest { allBooks ->
                            _books.value = RequestState.Success(
                                data = allBooks.sortedBy { it.isFavorite }
                            )
                        }
                }
            }
        }
    }

    fun toggleSortByFavorite() {
        _sortedByFavorite.value = !_sortedByFavorite.value
    }

    fun sendData(token: String) {
        viewModelScope.launch {
            pushNotificationClient.sendData(token)
        }
    }
}