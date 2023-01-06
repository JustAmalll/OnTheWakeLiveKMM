package dev.amal.onthewakelivekmm.feature_auth.domain.models

enum class AuthResult {
    Authorized,
    Unauthorized,
    UserAlreadyExist,
    IncorrectData,
    UnknownError
}