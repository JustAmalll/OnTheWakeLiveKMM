package dev.amal.onthewakelivekmm.feature_profile.domain.repository

import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.core.util.SimpleResource
import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile
import dev.amal.onthewakelivekmm.feature_profile.domain.module.UpdateProfileData

interface ProfileRepository {
    suspend fun getProfile(): Resource<Profile>
    suspend fun updateProfile(
        updateProfileData: UpdateProfileData
    ): SimpleResource
}