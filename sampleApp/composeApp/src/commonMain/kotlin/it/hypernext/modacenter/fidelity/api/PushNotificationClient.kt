package it.hypernext.modacenter.fidelity.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import it.hypernext.modacenter.fidelity.api.datamodel.DataUser
import it.hypernext.modacenter.fidelity.api.util.NetworkEError
import it.hypernext.modacenter.fidelity.api.util.Result
import kotlinx.serialization.SerializationException

class PushNotificationClient(
    private val httpClient: HttpClient
) {

    suspend fun sendData(token: String): Result<String, NetworkEError> {
        val response = try {
            httpClient.post(urlString = "https://app.erroridiconiazione.com/data") {
                contentType(ContentType.Application.Json)
                setBody(DataUser(token = token))
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkEError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkEError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> {
                val censoredText = response.body<String>()
                Result.Success(censoredText)
            }

            401 -> Result.Error(NetworkEError.UNAUTHORIZED)
            409 -> Result.Error(NetworkEError.CONFLICT)
            408 -> Result.Error(NetworkEError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkEError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkEError.SERVER_ERROR)
            else -> Result.Error(NetworkEError.UNKNOWN)
        }
    }
}