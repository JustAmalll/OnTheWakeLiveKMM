package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_register.presentation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.model.OtpResult
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.repository.OtpRepository
import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_register.RegisterEvent
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_register.RegisterViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AndroidRegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val otpRepository: OtpRepository,
    private val validationUseCase: ValidationUseCase
) : ViewModel() {

    private val _authResult = MutableSharedFlow<AuthResult>()
    val authResult = _authResult.asSharedFlow()

    private val _otpResult = MutableSharedFlow<OtpResult>()
    val otpResult = _otpResult.asSharedFlow()

    private val _navigateUpEvent = MutableSharedFlow<RegisterData>()
    val navigateUpEvent = _navigateUpEvent.asSharedFlow()

    var isLoading by mutableStateOf(false)

    private val viewModel by lazy {
        RegisterViewModel(validationUseCase = validationUseCase)
    }

    val state = viewModel.state

    fun onEvent(event: RegisterEvent) {
        viewModel.onEvent(event)
    }

    fun sendOtp(context: Context) {
        if (!viewModel.isValidationSuccess()) {
            return
        }

        viewModelScope.launch {
            isLoading = true

            val isUserAlreadyExists = repository.isUserAlreadyExists(
                phoneNumber = state.value.signUpPhoneNumber
            )

            if (!isUserAlreadyExists) {
                val result = otpRepository.sendOtp(
                    phoneNumber = state.value.signUpPhoneNumber.trim(),
                    context = context,
                    isResendAction = false
                )
                if (result == OtpResult.OtpSentSuccess)
                    _navigateUpEvent.emit(
                        RegisterData(
                            firstName = state.value.signUpFirstName.trim(),
                            lastName = state.value.signUpLastName.trim(),
                            phoneNumber = state.value.signUpPhoneNumber.trim(),
                            password = state.value.signUpPassword.trim()
                        )
                    )
                _otpResult.emit(result)
            } else {
                _authResult.emit(AuthResult.UserAlreadyExist)
            }
            isLoading = false
        }
    }
}