package dev.amal.onthewakelivekmm.android.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.data.repository.OtpRepositoryImpl
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.repository.OtpRepository
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
    fun provideOtpRepository(): OtpRepository =
        OtpRepositoryImpl(firebaseAuth = FirebaseAuth.getInstance())

    @Provides
    @Singleton
    fun provideValidationUseCase(
        @ApplicationContext context: Context
    ): ValidationUseCase = ValidationUseCase(context)
}