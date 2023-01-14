package dev.amal.onthewakelivekmm.feature_profile.presentation

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile
import dev.amal.onthewakelivekmm.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnProfileErrorSeen -> _state.update {
                it.copy(error = null)
            }
            is ProfileEvent.GetProfile -> getProfile()
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = profileRepository.getProfile()) {
                is Resource.Success -> {
                    result.data?.let { profile ->
                        _state.update {
                            it.copy(
                                profile = Profile(
                                    userId = profile.userId,
                                    firstName = profile.firstName,
                                    lastName = profile.lastName,
                                    profilePictureUri = profile.profilePictureUri,
                                    phoneNumber = profile.phoneNumber,
                                    telegram = profile.telegram,
                                    instagram = profile.instagram,
                                    dateOfBirth = profile.dateOfBirth
                                )
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(error = result.message ?: "Unknown Error") }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}