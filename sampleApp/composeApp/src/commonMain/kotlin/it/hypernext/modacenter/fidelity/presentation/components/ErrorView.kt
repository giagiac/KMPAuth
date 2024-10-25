package it.hypernext.modacenter.fidelity.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.api.util.NetworkEError
import it.hypernext.modacenter.fidelity.error_conflict
import it.hypernext.modacenter.fidelity.error_not_defined
import it.hypernext.modacenter.fidelity.error_payload_too_large
import it.hypernext.modacenter.fidelity.error_request_timeout
import it.hypernext.modacenter.fidelity.error_serialization
import it.hypernext.modacenter.fidelity.error_server_error
import it.hypernext.modacenter.fidelity.error_too_many_requests
import it.hypernext.modacenter.fidelity.error_unauthorized
import it.hypernext.modacenter.fidelity.error_unknown
import it.hypernext.modacenter.fidelity.no_internet
import it.hypernext.modacenter.fidelity.sharp_cell_wifi_24
import it.hypernext.modacenter.fidelity.sharp_question_mark_24
import it.hypernext.modacenter.fidelity.sharp_sentiment_worried_24
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TextRes(id: StringResource){
    Text(text = stringResource(id), color = Color.Red, fontSize = 24.sp)
}

@Composable
fun ErrorView(message: String? = null) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            when (message) {
                null -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_question_mark_24),
                        contentDescription = "no internet"
                    )
                    TextRes(Res.string.error_not_defined)
                }

                NetworkEError.NO_INTERNET.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp).fillMaxSize(),
                        painter = painterResource(Res.drawable.sharp_cell_wifi_24),
                        contentDescription = "no internet"
                    )
                    TextRes(Res.string.no_internet)
                }

                NetworkEError.REQUEST_TIMEOUT.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_sentiment_worried_24),
                        contentDescription = "request timeout"
                    )
                    TextRes(Res.string.error_request_timeout)
                }

                NetworkEError.SERVER_ERROR.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_sentiment_worried_24),
                        contentDescription = "server error"
                    )
                    TextRes(Res.string.error_server_error)
                }

                NetworkEError.CONFLICT.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_sentiment_worried_24),
                        contentDescription = "server error"
                    )
                    TextRes(Res.string.error_conflict)
                }

                NetworkEError.UNKNOWN.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_sentiment_worried_24),
                        contentDescription = "server error"
                    )
                    TextRes(Res.string.error_unknown)
                }

                NetworkEError.PAYLOAD_TOO_LARGE.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_sentiment_worried_24),
                        contentDescription = "server error"
                    )
                    TextRes(Res.string.error_payload_too_large)
                }

                NetworkEError.SERIALIZATION.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_sentiment_worried_24),
                        contentDescription = "server error"
                    )
                    TextRes(Res.string.error_serialization)
                }

                NetworkEError.UNAUTHORIZED.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_sentiment_worried_24),
                        contentDescription = "server error"
                    )
                    TextRes(Res.string.error_unauthorized)
                }

                NetworkEError.TOO_MANY_REQUESTS.toString() -> {
                    Icon(
                        modifier = Modifier.size(68.dp),
                        painter = painterResource(Res.drawable.sharp_sentiment_worried_24),
                        contentDescription = "too many requets"
                    )
                    TextRes(Res.string.error_too_many_requests)
                }
                else -> {
                    Text(text = message, color = Color.Red)
                }
            }
        }
    }
}