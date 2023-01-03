package dev.amal.onthewakelivekmm.feature_auth.domain.repository

import dev.amal.onthewakelivekmm.feature_auth.data.remote.request.CreateAccountRequest
import dev.amal.onthewakelivekmm.feature_auth.data.remote.request.AuthRequest
import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult

interface AuthRepository {
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun signIn(authRequest: AuthRequest): AuthResult<Unit>
    suspend fun signUp(accountRequest: CreateAccountRequest): AuthResult<Unit>
}