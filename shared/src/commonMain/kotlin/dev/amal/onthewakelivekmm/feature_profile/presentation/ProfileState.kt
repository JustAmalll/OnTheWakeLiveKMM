package dev.amal.onthewakelivekmm.feature_profile.presentation

import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val error: String? = null
)