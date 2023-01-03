package dev.amal.onthewakelivekmm.feature_auth.data.remote.request

@kotlinx.serialization.Serializable
data class CreateAccountRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val password: String
)