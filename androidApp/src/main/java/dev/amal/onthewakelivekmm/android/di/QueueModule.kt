package dev.amal.onthewakelivekmm.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.amal.onthewakelivekmm.feature_queue.data.repository.QueueServiceImpl
import dev.amal.onthewakelivekmm.feature_queue.data.repository.QueueSocketServiceImpl
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueSocketService
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QueueModule {

    @Provides
    @Singleton
    fun provideQueueService(
        client: HttpClient
    ): QueueService = QueueServiceImpl(client)

    @Provides
    @Singleton
    fun provideQueueSocketService(
        client: HttpClient
    ): QueueSocketService = QueueSocketServiceImpl(client)
}