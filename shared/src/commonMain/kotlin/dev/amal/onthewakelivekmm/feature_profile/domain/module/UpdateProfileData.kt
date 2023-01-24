package dev.amal.onthewakelivekmm.feature_profile.domain.module

@kotlinx.serialization.Serializable
data class UpdateProfileData(
    val firstName: String,
    val lastName: String,
    val instagram: String,
    val telegram: String,
    val dateOfBirth: String,
    val profilePictureUri: String?
)
