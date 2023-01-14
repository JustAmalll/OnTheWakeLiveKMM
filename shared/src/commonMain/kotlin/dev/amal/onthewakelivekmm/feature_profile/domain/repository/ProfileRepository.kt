package dev.amal.onthewakelivekmm.feature_profile.domain.repository

import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile

interface ProfileRepository {
    suspend fun getProfile(): Resource<Profile>
}