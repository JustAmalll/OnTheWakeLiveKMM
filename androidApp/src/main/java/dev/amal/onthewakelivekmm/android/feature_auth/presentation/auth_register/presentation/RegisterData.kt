package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_register.presentation

@kotlinx.serialization.Serializable
data class RegisterData(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val password: String
)