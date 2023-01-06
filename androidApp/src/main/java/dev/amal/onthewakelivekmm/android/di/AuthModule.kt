package dev.amal.onthewakelivekmm.android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.feature_auth.data.repository.AuthRepositoryImpl
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
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

    @Provides
    @Singleton
    fun provideValidationUseCase(
        @ApplicationContext context: Context
    ): ValidationUseCase = ValidationUseCase(context)
}