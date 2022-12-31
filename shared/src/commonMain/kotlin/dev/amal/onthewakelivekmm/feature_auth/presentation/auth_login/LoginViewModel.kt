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

    init {
        viewModelScope.launch {
            repository.authenticate()
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SignInPhoneNumberChanged -> _state.update {
                it.copy(signInPhoneNumber = event.value)
            }
            is LoginEvent.SignInPasswordChanged -> _state.update {
                it.copy(signInPassword = event.value)
            }
            is LoginEvent.SignIn -> signIn(
                phoneNumber = state.value.signInPhoneNumber,
                password = state.value.signInPassword
            )
        }
    }

    private fun signIn(phoneNumber: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = repository.signIn(
                AuthRequest(
                    phoneNumber = phoneNumber.trim(),
                    password = password.trim()
                )
            )
            println("auth result  is $result")
            _state.update {
                it.copy(loginResult = result, isLoading = false)
            }
        }
    }
}