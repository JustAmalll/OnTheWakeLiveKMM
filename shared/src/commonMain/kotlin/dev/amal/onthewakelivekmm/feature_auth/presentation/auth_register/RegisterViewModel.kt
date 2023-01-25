package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_register

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel(
    private val validationUseCase: ValidationUseCase
) {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.SignUpFirstNameChanged -> _state.update {
                it.copy(signUpFirstName = event.value)
            }
            is RegisterEvent.SignUpLastNameChanged -> _state.update {
                it.copy(signUpLastName = event.value)
            }
            is RegisterEvent.SignUpPhoneNumberChanged -> _state.update {
                it.copy(signUpPhoneNumber = event.value)
            }
            is RegisterEvent.SignUpPasswordChanged -> _state.update {
                it.copy(signUpPassword = event.value)
            }
        }
    }

    fun isValidationSuccess(): Boolean {
        val firstNameResult = validationUseCase.validateFirstName(state.value.signUpFirstName)
        val lastNameResult = validationUseCase.validateLastName(state.value.signUpLastName)
        val phoneNumberResult = validationUseCase.validatePhoneNumber(state.value.signUpPhoneNumber)
        val passwordResult = validationUseCase.validatePassword(state.value.signUpPassword)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            phoneNumberResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            _state.update {
                it.copy(
                    signUpFirstNameError = firstNameResult.errorMessage,
                    signUpLastNameError = lastNameResult.errorMessage,
                    signUpPhoneNumberError = phoneNumberResult.errorMessage,
                    signUpPasswordError = passwordResult.errorMessage
                )
            }
        } else {
            _state.update {
                it.copy(
                    signUpFirstNameError = null,
                    signUpLastNameError = null,
                    signUpPhoneNumberError = null,
                    signUpPasswordError = null
                )
            }
        }
        return !hasError
    }
}