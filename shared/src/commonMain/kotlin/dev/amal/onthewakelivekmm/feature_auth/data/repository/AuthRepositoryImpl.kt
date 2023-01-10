package dev.amal.onthewakelivekmm.feature_auth.data.repository

import dev.amal.onthewakelivekmm.feature_auth.data.remote.request.CreateAccountRequest
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.util.Constants.BASE_URL
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_FIRST_NAME
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_JWT_TOKEN
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_USER_ID
import dev.amal.onthewakelivekmm.feature_auth.data.remote.request.AuthRequest
import dev.amal.onthewakelivekmm.feature_auth.data.remote.response.AuthResponse
import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    override suspend fun signUp(
        accountRequest: CreateAccountRequest
    ): AuthResult = try {
        val result = client.post("$BASE_URL/signup") {
            setBody(accountRequest)
        }
        signIn(
            AuthRequest(
                phoneNumber = accountRequest.phoneNumber,
                password = accountRequest.password
            )
        )
        when (result.status.value) {
            in 200..299 -> AuthResult.Authorized
            401 -> AuthResult.Unauthorized
            409 -> AuthResult.UserAlreadyExist
            500 -> AuthResult.UnknownError
            else -> AuthResult.UnknownError
        }
    } catch (exception: Exception) {
        AuthResult.UnknownError
    }

    override suspend fun signIn(
        authRequest: AuthRequest
    ): AuthResult {
        val result = try {
            client.post("$BASE_URL/signin") {
                setBody(authRequest)
            }
        } catch (exception: Exception) {
            return AuthResult.UnknownError
        }

        when (result.status.value) {
            in 200..299 -> Unit
            401 -> return AuthResult.Unauthorized
            409 -> return AuthResult.IncorrectData
            500 -> return AuthResult.UnknownError
            else -> return AuthResult.UnknownError
        }

        return try {
            val authResponse = result.body<AuthResponse>()
            preferenceManager.setString(PREFS_JWT_TOKEN, authResponse.token)
            preferenceManager.setString(PREFS_USER_ID, authResponse.userId)
            preferenceManager.setString(PREFS_FIRST_NAME, authResponse.firstName)

            AuthResult.Authorized
        } catch (exception: Exception) {
            AuthResult.UnknownError
        }
    }

    override suspend fun isUserAlreadyExists(
        phoneNumber: String
    ): Boolean = try {
        val request = client.get("$BASE_URL/isUserAlreadyExists") {
            parameter("phoneNumber", phoneNumber)
        }
        request.body()
    } catch (exception: Exception) {
        exception.printStackTrace()
        false
    }

    override suspend fun authenticate(): AuthResult {
        return try {
            val token = preferenceManager.getString(PREFS_JWT_TOKEN)
                ?: return AuthResult.Unauthorized

            val result = client.get("$BASE_URL/authenticate") {
                header("Bearer", token)
            }

            when (result.status.value) {
                in 200..299 -> AuthResult.Authorized
                401 -> AuthResult.Unauthorized
                else -> AuthResult.UnknownError
            }
        } catch (exception: Exception) {
            AuthResult.UnknownError
        }
    }
}