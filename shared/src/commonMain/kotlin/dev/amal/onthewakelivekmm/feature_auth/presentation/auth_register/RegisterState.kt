package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_register

data class RegisterState(
    val isLoading: Boolean = false,
    val signUpFirstName: String = "",
    val signUpFirstNameError: String? = null,
    val signUpLastName: String = "",
    val signUpLastNameError: String? = null,
    val signUpPhoneNumber: String = "",
    val signUpPhoneNumberError: String? = null,
    val signUpPassword: String = "",
    val signUpPasswordError: String? = null
)