package dev.amal.onthewakelivekmm.core.data.remote

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

actual class HttpClientFactory {
    actual fun create(): HttpClient {
        return HttpClient(Darwin) {
            install(WebSockets)
            install(Logging)
            install(ContentNegotiation) {
                json()
            }
            install(DefaultRequest) {
                header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ1c2VycyIsImlzcyI6Imh0dHA6Ly8wLjAuMC4wOjgwODAiLCJleHAiOjE3MDM4NDk5NjIsInVzZXJJZCI6IjYzYTlhZjJhYTIzZjRlNDUzNzE5MDk2NyJ9.V6KEjAkydX8eXXbYOJbw72HKDb8n-TOYQ8UZsSLmCLw")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
}