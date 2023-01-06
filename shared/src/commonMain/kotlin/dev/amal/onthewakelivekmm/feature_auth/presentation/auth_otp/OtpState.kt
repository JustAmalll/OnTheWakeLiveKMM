package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_otp

import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult

data class OtpState(
    val isLoading: Boolean = false,
    val signUpResult: AuthResult? = null,
    val otp: String = "",
    val otpError: String? = null
)