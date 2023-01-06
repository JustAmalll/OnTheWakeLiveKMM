package dev.amal.onthewakelivekmm.feature_auth.presentation.auth_login

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.feature_auth.data.remote.request.AuthRequest
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(LoginState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SignInPhoneNumberChanged -> _state.update {
                it.copy(signInPhoneNumber = event.value)
            }
            is LoginEvent.SignInPasswordChanged -> _state.update {
                it.copy(signInPassword = event.value)
            }
            is LoginEvent.OnLoginResultSeen -> _state.update {
                it.copy(loginResult = null)
            }
            is LoginEvent.SignIn -> signIn()
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = repository.signIn(
                AuthRequest(
                    phoneNumber = state.value.signInPhoneNumber.trim(),
                    password = state.value.signInPassword.trim()
                )
            )
            println("auth result is $result")
            _state.update { it.copy(loginResult = result, isLoading = false) }
        }
    }
}