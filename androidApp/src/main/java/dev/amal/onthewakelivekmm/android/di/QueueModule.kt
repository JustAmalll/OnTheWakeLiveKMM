package dev.amal.onthewakelivekmm.android.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_JWT_TOKEN
import dev.amal.onthewakelivekmm.feature_queue.data.repository.QueueServiceImpl
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QueueModule {

    @Provides
    @Singleton
    fun provideHttpClient(prefs: SharedPreferences): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
            install(DefaultRequest) {
                val token = prefs.getString(PREFS_JWT_TOKEN, "")
                header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ1c2VycyIsImlzcyI6Imh0dHA6Ly8wLjAuMC4wOjgwODAiLCJleHAiOjE3MDM4NDk5NjIsInVzZXJJZCI6IjYzYTlhZjJhYTIzZjRlNDUzNzE5MDk2NyJ9.V6KEjAkydX8eXXbYOJbw72HKDb8n-TOYQ8UZsSLmCLw")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

    @Provides
    @Singleton
    fun provideQueueService(
        client: HttpClient
    ): QueueService = QueueServiceImpl(client)

}