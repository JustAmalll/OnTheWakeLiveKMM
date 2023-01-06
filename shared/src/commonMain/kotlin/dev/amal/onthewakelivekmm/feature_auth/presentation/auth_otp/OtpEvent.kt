package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_otp

sealed class OtpEvent {
    data class OtpChanged(val value: String) : OtpEvent()
    data class SignUp(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val password: String
    ) : OtpEvent()
}