package dev.amal.onthewakelivekmm.android.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.amal.onthewakelivekmm.android.feature_profile.data.repository.FirebaseStorageRepositoryImpl
import dev.amal.onthewakelivekmm.android.feature_profile.domain.repository.FirebaseStorageRepository
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.feature_profile.data.repository.ProfileRepositoryImpl
import dev.amal.onthewakelivekmm.feature_profile.domain.repository.ProfileRepository
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(
        httpClient: HttpClient, preferenceManager: PreferenceManager
    ): ProfileRepository = ProfileRepositoryImpl(httpClient, preferenceManager)

    @Provides
    @Singleton
    fun provideAndroidProfileRepository(): FirebaseStorageRepository =
        FirebaseStorageRepositoryImpl(Firebase.storage)
}