package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.presentation

data class AndroidOtpState(
    val isLoading: Boolean = false,
    val signUpFirstName: String = "",
    val signUpLastName: String = "",
    val signUpPhoneNumber: String = "",
    val signUpPassword: String = "",
)