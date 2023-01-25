package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_otp

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.feature_auth.data.remote.request.CreateAccountRequest
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpViewModel(
    private val repository: AuthRepository,
    private val validationUseCase: ValidationUseCase,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(OtpState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.OtpChanged -> _state.update {
                it.copy(otp = event.value)
            }
            is OtpEvent.SignUp -> signUp(
                createAccountRequest = event.createAccountRequest
            )
            is OtpEvent.OnSignUpResultSeen -> _state.update {
                it.copy(signUpResult = null)
            }
        }
    }

    private fun signUp(createAccountRequest: CreateAccountRequest) {
        viewModelScope.launch {
            val signUpResult = repository.signUp(createAccountRequest)
            _state.update { it.copy(signUpResult = signUpResult) }
        }
    }

    fun isOtpValidationSuccess(): Boolean {
        val otpValidationResult = validationUseCase.validateOtp(state.value.otp)

        if (!otpValidationResult.successful) {
            _state.update { it.copy(otpError = otpValidationResult.errorMessage) }
        } else {
            _state.update { it.copy(otpError = null) }
        }

        return otpValidationResult.successful
    }
}