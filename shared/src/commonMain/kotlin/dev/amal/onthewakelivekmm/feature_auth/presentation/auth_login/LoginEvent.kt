package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_login

sealed class LoginEvent {
    data class SignInPhoneNumberChanged(val value: String) : LoginEvent()
    data class SignInPasswordChanged(val value: String) : LoginEvent()
    object OnLoginResultSeen : LoginEvent()
    object SignIn : LoginEvent()
}