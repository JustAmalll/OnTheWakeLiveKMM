package com.onthewake.onthewakelive.feature_auth.data.remote.request

data class CreateAccountRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val password: String
)