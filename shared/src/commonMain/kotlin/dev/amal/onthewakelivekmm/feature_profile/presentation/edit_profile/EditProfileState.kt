package dev.amal.onthewakelivekmm.feature_profile.presentation.edit_profile

data class EditProfileState(
    val isLoading: Boolean = false,
    val resultMessage: String? = null,
    val userId: String? = null,
    val firstName: String = "",
    val firsNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val telegram: String = "",
    val instagram: String = "",
    val profilePictureUri: String = "",
    val dateOfBirth: String = "",
    val dateOfBirthError: String? = null
)