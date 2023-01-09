package dev.amal.onthewakelivekmm.core.data.remote

import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.util.Constants
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

actual class HttpClientFactory(
    private val preferenceManager: PreferenceManager
) {
    actual fun create(): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets)
            install(Logging)
            install(ContentNegotiation) {
                json()
            }
            install(DefaultRequest) {
                val token = preferenceManager.getString(Constants.PREFS_JWT_TOKEN) ?: ""
                header("Authorization", "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
}