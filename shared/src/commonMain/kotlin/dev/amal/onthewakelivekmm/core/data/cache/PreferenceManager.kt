package dev.amal.onthewakelivekmm.core.data.cache

import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_ACCOUNT_DATA
import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PreferenceManager(
    multiplatformSettingsWrapper: MultiplatformSettingsWrapper
) {
    private val observableSettings = multiplatformSettingsWrapper.createSettings()

    fun setString(key: String, value: String) =
        observableSettings.putString(key = key, value = value)

    fun getString(key: String) =
        observableSettings.getStringOrNull(key = key)

    fun getProfile(): Profile? {
        val profileJson = observableSettings.getStringOrNull(PREFS_ACCOUNT_DATA) ?: return null
        return Json.decodeFromString<Profile>(profileJson)
    }
}
