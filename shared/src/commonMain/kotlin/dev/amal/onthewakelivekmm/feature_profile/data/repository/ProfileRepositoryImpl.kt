package dev.amal.onthewakelivekmm.feature_profile.data.repository

import com.onthewake.onthewakelive.core.data.dto.response.BasicApiResponse
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.util.Constants.BASE_URL
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_ACCOUNT_DATA
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_profile.data.remote.response.ProfileResponse
import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile
import dev.amal.onthewakelivekmm.feature_profile.domain.repository.ProfileRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfileRepositoryImpl(
    private val client: HttpClient,
    private val preferenceManager: PreferenceManager
) : ProfileRepository {

    override suspend fun getProfile(): Resource<Profile> {

        val profileJson = preferenceManager.getString(PREFS_ACCOUNT_DATA)
        if (profileJson != null) {
            val profile = Json.decodeFromString<Profile>(profileJson)
            return Resource.Success(profile)
        }

        val result = try {
            client.get("$BASE_URL/api/user/profile")
        } catch (exception: IOException) {
            return Resource.Error("Oops! Couldn't reach server. Check your internet connection.")
        } catch (exception: Exception) {
            return Resource.Error("Oops! Something went wrong. Please try again.")
        }

        when (result.status.value) {
            in 200..299 -> Unit
            else -> return Resource.Error("Oops! Something went wrong. Please try again.")
        }

        return try {
           val profileResponse = result.body<BasicApiResponse<ProfileResponse>>()

            if (profileResponse.successful) {
                val profile = profileResponse.data?.toProfile()
                val profileJsonString = Json.encodeToString(profile)

                preferenceManager.setString(PREFS_ACCOUNT_DATA, profileJsonString)
                Resource.Success(profile)
            } else {
                profileResponse.message?.let { msg -> Resource.Error(msg) }
                    ?: Resource.Error("Oops! Something went wrong. Please try again.")
            }
        } catch (exception: Exception) {
            Resource.Error("Oops! Something went wrong. Please try again.")
        }
    }
}