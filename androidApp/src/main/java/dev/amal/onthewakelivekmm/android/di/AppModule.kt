package dev.amal.onthewakelivekmm.android.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences =
        app.getSharedPreferences("prefs", MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideImageLoader(app: Application): ImageLoader =
        ImageLoader.Builder(app)
            .crossfade(true)
            .build()
}