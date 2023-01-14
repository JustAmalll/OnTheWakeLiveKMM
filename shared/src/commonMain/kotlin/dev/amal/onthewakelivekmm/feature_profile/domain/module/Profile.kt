package dev.amal.onthewakelivekmm.feature_profile.domain.module

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUri: String,
    val phoneNumber: String,
    val telegram: String,
    val instagram: String,
    val dateOfBirth: String
)
