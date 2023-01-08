package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.presentation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.android.core.presentation.utils.Constants.REGISTER_DATA_ARGUMENT_KEY
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.model.OtpResult
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.repository.OtpRepository
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_register.presentation.RegisterData
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_otp.OtpEvent
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_otp.OtpViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class AndroidOtpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val otpRepository: OtpRepository,
    private val validationUseCase: ValidationUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var androidOtpState by mutableStateOf(AndroidOtpState())
        private set

    private val _otpResult = MutableSharedFlow<OtpResult>()
    val otpResult = _otpResult.asSharedFlow()

    private val viewModel by lazy {
        OtpViewModel(
            repository = authRepository,
            validationUseCase = validationUseCase,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    init {
        savedStateHandle.get<String>(REGISTER_DATA_ARGUMENT_KEY)?.let { registerDataJson ->
            val registerData = Json.decodeFromString<RegisterData>(registerDataJson)
            androidOtpState = androidOtpState.copy(
                signUpFirstName = registerData.firstName,
                signUpLastName = registerData.lastName,
                signUpPhoneNumber = registerData.phoneNumber,
                signUpPassword = registerData.password
            )
        }
    }

    fun onEvent(event: OtpEvent) {
        viewModel.onEvent(event)
    }

    fun verifyOtpAndSignUp() {

        if (viewModel.isOtpValidationSuccess()) {
            viewModelScope.launch {
                androidOtpState = androidOtpState.copy(isLoading = true)

                val otpResult = otpRepository.verifyOtp(state.value.otp)

                println("verify otp result $otpResult")

                if (otpResult == OtpResult.OtpVerified) {
                    onEvent(
                        OtpEvent.SignUp(
                            firstName = androidOtpState.signUpFirstName,
                            lastName = androidOtpState.signUpLastName,
                            phoneNumber = androidOtpState.signUpPhoneNumber,
                            password = androidOtpState.signUpPassword
                        )
                    )
                } else {
                    _otpResult.emit(OtpResult.IncorrectOtp)
                }
                androidOtpState = androidOtpState.copy(isLoading = false)
            }
        }
    }

    fun resendCode(context: Context) {
        viewModelScope.launch {
            val resendResult = otpRepository.sendOtp(
                phoneNumber = androidOtpState.signUpPhoneNumber,
                context = context,
                isResendAction = true
            )
            println("resending otp $resendResult")
            _otpResult.emit(resendResult)
        }
    }
}