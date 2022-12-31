package dev.amal.onthewakelivekmm.core.data.cache

import android.content.Context
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings

actual class MultiplatformSettingsWrapper(
    private val context: Context
) {
    actual fun createSettings(): ObservableSettings {
        val delegate = context.getSharedPreferences(
            "token_prefs", Context.MODE_PRIVATE
        )
        return SharedPreferencesSettings(delegate)
    }
}