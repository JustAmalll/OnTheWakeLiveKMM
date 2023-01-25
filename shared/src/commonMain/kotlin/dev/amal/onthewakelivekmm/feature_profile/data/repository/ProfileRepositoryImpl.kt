package dev.amal.onthewakelivekmm.feature_profile.data.repository

import com.onthewake.onthewakelive.core.data.dto.response.BasicApiResponse
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.util.Constants.BASE_URL
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_ACCOUNT_DATA
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_FIRST_NAME
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.core.util.SimpleResource
import dev.amal.onthewakelivekmm.feature_profile.data.remote.response.ProfileResponse
import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile
import dev.amal.onthewakelivekmm.feature_profile.domain.module.UpdateProfileData
import dev.amal.onthewakelivekmm.feature_profile.domain.repository.ProfileRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfileRepositoryImpl(
    private val client: HttpClient,
    private val preferenceManager: PreferenceManager
) : ProfileRepository {

    override suspend fun getProfile(): Resource<Profile> {
        val profile = preferenceManager.getProfile()
        if (profile != null) return Resource.Success(profile)

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
            val response = result.body<BasicApiResponse<ProfileResponse>>()

            if (response.successful) {
                val profileResponse = response.data?.toProfile()
                val profileJsonString = Json.encodeToString(profileResponse)

                preferenceManager.setString(PREFS_ACCOUNT_DATA, profileJsonString)
                Resource.Success(profileResponse)
            } else {
                response.message?.let { msg -> Resource.Error(msg) }
                    ?: Resource.Error("Oops! Something went wrong. Please try again.")
            }
        } catch (exception: Exception) {
            Resource.Error("Oops! Something went wrong. Please try again.")
        }
    }

    private fun isThereIsAnythingToUpdate(
        oldProfileData: Profile,
        profileDataToUpdate: UpdateProfileData
    ): Boolean = !(oldProfileData.firstName.trim() == profileDataToUpdate.firstName.trim() &&
            oldProfileData.lastName.trim() == profileDataToUpdate.lastName.trim() &&
            oldProfileData.profilePictureUri == profileDataToUpdate.profilePictureUri &&
            oldProfileData.instagram.trim() == profileDataToUpdate.instagram.trim() &&
            oldProfileData.telegram.trim() == profileDataToUpdate.telegram.trim() &&
            oldProfileData.dateOfBirth == profileDataToUpdate.dateOfBirth)

    override suspend fun updateProfile(
        updateProfileData: UpdateProfileData
    ): SimpleResource {

        val profile = preferenceManager.getProfile()
            ?: return Resource.Error("Oops! Something went wrong. Please try again.")

        val oldProfilePictureUri = profile.profilePictureUri
        val newProfilePictureUri = updateProfileData.profilePictureUri

        val profilePictureUri = newProfilePictureUri ?: oldProfilePictureUri

        val isThereIsAnythingToUpdate = isThereIsAnythingToUpdate(
            oldProfileData = profile,
            profileDataToUpdate = updateProfileData.copy(profilePictureUri = profilePictureUri)
        )
        if (!isThereIsAnythingToUpdate)
            return Resource.Error("There is nothing to update")

        val result = try {
            client.put("$BASE_URL/api/user/update") {
                setBody(updateProfileData.copy(profilePictureUri = profilePictureUri))
            }
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
            val updateProfileResponse = result.body<BasicApiResponse<Unit>>()

            if (updateProfileResponse.successful) {
                val profileJsonString = Json.encodeToString(
                    Profile(
                        userId = profile.userId,
                        firstName = updateProfileData.firstName,
                        lastName = updateProfileData.lastName,
                        profilePictureUri = profilePictureUri,
                        phoneNumber = profile.phoneNumber,
                        telegram = updateProfileData.telegram,
                        instagram = updateProfileData.instagram,
                        dateOfBirth = updateProfileData.dateOfBirth
                    )
                )

                preferenceManager.setString(PREFS_ACCOUNT_DATA, profileJsonString)
                preferenceManager.setString(PREFS_FIRST_NAME, updateProfileData.firstName)
                Resource.Success(Unit)
            } else {
                updateProfileResponse.message?.let { msg -> Resource.Error(msg) }
                    ?: Resource.Error("Oops! Something went wrong. Please try again.")
            }
        } catch (exception: Exception) {
            Resource.Error("Oops! Something went wrong. Please try again.")
        }
    }
}