package dev.amal.onthewakelivekmm.feature_auth.data.remote.request

@kotlinx.serialization.Serializable
data class AuthRequest(
    val phoneNumber: String,
    val password: String
)