package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_register

sealed class RegisterEvent {
    data class SignUpFirstNameChanged(val value: String) : RegisterEvent()
    data class SignUpLastNameChanged(val value: String) : RegisterEvent()
    data class SignUpPhoneNumberChanged(val value: String) : RegisterEvent()
    data class SignUpPasswordChanged(val value: String) : RegisterEvent()
}