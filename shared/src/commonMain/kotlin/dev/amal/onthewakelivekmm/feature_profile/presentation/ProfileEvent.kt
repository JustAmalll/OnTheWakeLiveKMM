package dev.amal.onthewakelivekmm.feature_profile.presentation

sealed class ProfileEvent {
    object GetProfile: ProfileEvent()
    object OnProfileErrorSeen: ProfileEvent()
}