package dev.amal.onthewakelivekmm.feature_profile.presentation.edit_profile

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import dev.amal.onthewakelivekmm.feature_profile.domain.module.UpdateProfileData
import dev.amal.onthewakelivekmm.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val validationUseCase: ValidationUseCase,
    private val profileRepository: ProfileRepository,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.GetProfile -> {
                getProfile()
            }
            is EditProfileEvent.OnResultSeen -> _state.update {
                it.copy(resultMessage = null)
            }
            is EditProfileEvent.EditProfileFirstNameChanged -> _state.update {
                it.copy(firstName = event.value)
            }
            is EditProfileEvent.EditProfileLastNameChanged -> _state.update {
                it.copy(lastName = event.value)
            }
            is EditProfileEvent.EditProfileTelegramChanged -> _state.update {
                it.copy(telegram = event.value)
            }
            is EditProfileEvent.EditProfileInstagramChanged -> _state.update {
                it.copy(instagram = event.value)
            }
            is EditProfileEvent.EditProfileDateOfBirthChanged -> _state.update {
                it.copy(dateOfBirth = event.value)
            }
            is EditProfileEvent.EditProfile -> editProfile(
                profilePictureUri = event.profilePictureUri
            )
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
                                userId = profile.userId,
                                firstName = profile.firstName,
                                lastName = profile.lastName,
                                profilePictureUri = profile.profilePictureUri,
                                telegram = profile.telegram,
                                instagram = profile.instagram,
                                dateOfBirth = profile.dateOfBirth
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(resultMessage = result.message ?: "Unknown Error") }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun editProfile(profilePictureUri: String?) {
        val firstNameResult = validationUseCase.validateFirstName(state.value.firstName)
        val lastNameResult = validationUseCase.validateLastName(state.value.lastName)
        val dateOfBirthResult = validationUseCase.validateDateOfBirth(state.value.dateOfBirth)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            dateOfBirthResult
        ).any { !it.successful }

        if (hasError) {
            _state.update {
                it.copy(
                    firsNameError = firstNameResult.errorMessage,
                    lastNameError = lastNameResult.errorMessage,
                    dateOfBirthError = dateOfBirthResult.errorMessage
                )
            }
            return
        } else {
            _state.update {
                it.copy(
                    firsNameError = null,
                    lastNameError = null,
                    dateOfBirthError = null
                )
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = profileRepository.updateProfile(
                updateProfileData = UpdateProfileData(
                    firstName = state.value.firstName.trim(),
                    lastName = state.value.lastName.trim(),
                    instagram = state.value.instagram.trim(),
                    telegram = state.value.telegram.trim(),
                    dateOfBirth = state.value.dateOfBirth,
                    profilePictureUri = profilePictureUri
                )
            )

            when (result) {
                is Resource.Success -> _state.update {
                    it.copy(resultMessage = "Successfully updated profile")
                }
                is Resource.Error -> _state.update {
                    it.copy(resultMessage = result.message ?: "Unknown Error")
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }
}