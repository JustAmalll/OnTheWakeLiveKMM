package dev.amal.onthewakelivekmm.feature_profile.presentation.edit_profile

sealed class EditProfileEvent {
    object GetProfile: EditProfileEvent()
    object OnResultSeen: EditProfileEvent()
    data class EditProfileFirstNameChanged(val value: String): EditProfileEvent()
    data class EditProfileLastNameChanged(val value: String): EditProfileEvent()
    data class EditProfileTelegramChanged(val value: String): EditProfileEvent()
    data class EditProfileInstagramChanged(val value: String): EditProfileEvent()
    data class EditProfileDateOfBirthChanged(val value: String): EditProfileEvent()
    data class EditProfile(val profilePictureUri: String? = null): EditProfileEvent()
}