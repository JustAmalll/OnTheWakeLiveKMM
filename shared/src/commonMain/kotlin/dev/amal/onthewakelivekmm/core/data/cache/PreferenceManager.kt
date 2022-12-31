package dev.amal.onthewakelivekmm.core.data.cache

class PreferenceManager(
    multiplatformSettingsWrapper: MultiplatformSettingsWrapper
) {

    private val observableSettings = multiplatformSettingsWrapper.createSettings()

    fun setString(key: String, value: String) =
        observableSettings.putString(key = key, value = value)

    fun getString(key: String) =
        observableSettings.getStringOrNull(key = key)

    fun clearPreferences() = observableSettings.clear()
}