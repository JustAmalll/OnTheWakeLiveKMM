package dev.amal.onthewakelivekmm.android.feature_profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_profile.domain.repository.ProfileRepository
import dev.amal.onthewakelivekmm.feature_profile.presentation.profile.ProfileEvent
import dev.amal.onthewakelivekmm.feature_profile.presentation.profile.ProfileViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val viewModel by lazy {
        ProfileViewModel(
            profileRepository = profileRepository,
            authRepository = authRepository,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: ProfileEvent) {
        viewModel.onEvent(event)
    }
}