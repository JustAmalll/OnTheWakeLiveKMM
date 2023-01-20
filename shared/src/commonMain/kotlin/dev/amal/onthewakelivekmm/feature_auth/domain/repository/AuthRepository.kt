package dev.amal.onthewakelivekmm.feature_auth.domain.repository

import dev.amal.onthewakelivekmm.feature_auth.data.remote.request.AuthRequest
import dev.amal.onthewakelivekmm.feature_auth.data.remote.request.CreateAccountRequest
import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult

interface AuthRepository {
    suspend fun authenticate(): AuthResult
    suspend fun signIn(authRequest: AuthRequest): AuthResult
    suspend fun signUp(accountRequest: CreateAccountRequest): AuthResult
    suspend fun isUserAlreadyExists(phoneNumber: String): Boolean
    fun logout()
}