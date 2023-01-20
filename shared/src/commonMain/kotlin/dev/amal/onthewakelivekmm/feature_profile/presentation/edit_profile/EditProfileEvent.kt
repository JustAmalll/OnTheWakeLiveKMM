package dev.amal.onthewakelivekmm.feature_profile.presentation.edit_profile

import dev.amal.onthewakelivekmm.feature_profile.domain.module.UpdateProfileData

sealed class EditProfileEvent {
    object GetProfile: EditProfileEvent()
    object OnResultSeen: EditProfileEvent()
    data class EditProfileFirstNameChanged(val value: String): EditProfileEvent()
    data class EditProfileLastNameChanged(val value: String): EditProfileEvent()
    data class EditProfileTelegramChanged(val value: String): EditProfileEvent()
    data class EditProfileInstagramChanged(val value: String): EditProfileEvent()
    data class EditProfileDateOfBirthChanged(val value: String): EditProfileEvent()
    data class EditProfile(val updateProfileData: UpdateProfileData): EditProfileEvent()
}