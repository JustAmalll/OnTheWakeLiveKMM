package dev.amal.onthewakelivekmm.feature_profile.presentation.profile

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnProfileErrorSeen -> _state.update { it.copy(error = null) }
            ProfileEvent.GetProfile -> getProfile()
            ProfileEvent.Logout -> authRepository.logout()
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = profileRepository.getProfile()) {
                is Resource.Success -> {
                    _state.update { it.copy(profile = result.data) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(error = result.message ?: "Unknown Error") }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}