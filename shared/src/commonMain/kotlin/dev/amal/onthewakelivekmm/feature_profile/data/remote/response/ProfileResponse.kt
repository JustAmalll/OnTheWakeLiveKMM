package dev.amal.onthewakelivekmm.feature_profile.data.remote.response

import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUri: String,
    val phoneNumber: String,
    val telegram: String,
    val instagram: String,
    val dateOfBirth: String
) {
    fun toProfile(): Profile = Profile(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        profilePictureUri = profilePictureUri,
        phoneNumber = phoneNumber,
        telegram = telegram,
        instagram = instagram,
        dateOfBirth = dateOfBirth
    )
}