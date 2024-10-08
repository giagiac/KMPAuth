package it.hypernext.modacenter.fidelity.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

//In shared/androidMain
@OptIn(ExperimentalSerializationApi::class)
actual fun createHttpClient() = HttpClient(Darwin) {
    //Timeout plugin to set up timeout milliseconds for client
    install(HttpTimeout) {
        socketTimeoutMillis = 60_000
        requestTimeoutMillis = 60_000
    }
    //Logging plugin combined with kermit(KMP Logger library)
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    //We can configure the BASE_URL and also
    //the deafult headers by defaultRequest builder
    defaultRequest {
        header("Content-Type", "application/json")
        header("Authorization", "Bearer ${"TEST"}")
        url("https://api.openai.com/v1/")
    }
    //ContentNegotiation plugin for negotiationing media types between the client and server
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
}