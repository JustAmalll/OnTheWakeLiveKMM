package dev.amal.onthewakelivekmm.feature_profile.presentation.profile

sealed class ProfileEvent {
    object GetProfile: ProfileEvent()
    object OnProfileErrorSeen: ProfileEvent()
    object Logout: ProfileEvent()
}