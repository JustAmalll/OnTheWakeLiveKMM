package dev.amal.onthewakelivekmm.di

import dev.amal.onthewakelivekmm.core.data.remote.HttpClientFactory
import dev.amal.onthewakelivekmm.feature_queue.data.repository.QueueServiceImpl
import dev.amal.onthewakelivekmm.feature_queue.data.repository.QueueSocketServiceImpl
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueSocketService

class AppModule {

    val queueService: QueueService by lazy {
        QueueServiceImpl(
            HttpClientFactory().create()
        )
    }

    val queueSocketService: QueueSocketService by lazy {
        QueueSocketServiceImpl(
            HttpClientFactory().create()
        )
    }
}