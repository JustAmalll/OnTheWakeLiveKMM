package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_login

import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult

data class LoginState(
    val isLoading: Boolean = false,
    val loginResult: AuthResult? = null,
    val signInPhoneNumber: String = "",
    val signInPhoneNumberError: String? = null,
    val signInPassword: String = "",
    val signInPasswordError: String? = null
)