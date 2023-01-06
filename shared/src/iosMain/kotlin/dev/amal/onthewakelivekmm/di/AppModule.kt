package dev.amal.onthewakelivekmm.di

import dev.amal.onthewakelivekmm.core.data.cache.MultiplatformSettingsWrapper
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.data.remote.HttpClientFactory
import dev.amal.onthewakelivekmm.feature_auth.data.repository.AuthRepositoryImpl
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import dev.amal.onthewakelivekmm.feature_queue.data.repository.QueueServiceImpl
import dev.amal.onthewakelivekmm.feature_queue.data.repository.QueueSocketServiceImpl
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueSocketService

class AppModule {

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            client = HttpClientFactory().create(),
            preferenceManager = PreferenceManager(
                MultiplatformSettingsWrapper()
            )
        )
    }

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

    val validationUseCase: ValidationUseCase by lazy {
        ValidationUseCase()
    }
}