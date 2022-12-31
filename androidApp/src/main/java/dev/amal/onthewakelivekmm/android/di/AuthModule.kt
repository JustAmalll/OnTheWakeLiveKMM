package dev.amal.onthewakelivekmm.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.feature_auth.data.repository.AuthRepositoryImpl
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        client: HttpClient, preferenceManager: PreferenceManager
    ): AuthRepository = AuthRepositoryImpl(client, preferenceManager)
}