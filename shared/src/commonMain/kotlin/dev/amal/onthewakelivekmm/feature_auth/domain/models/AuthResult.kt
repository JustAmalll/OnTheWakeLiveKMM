package dev.amal.onthewakelivekmm.feature_auth.domain.models

sealed class AuthResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class Unauthorized<T>: AuthResult<T>()
    class UserAlreadyExist<T>: AuthResult<T>()
    class IncorrectData<T>: AuthResult<T>()
    class UnknownError<T>: AuthResult<T>()
}