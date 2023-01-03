package dev.amal.onthewakelivekmm.feature_splash.presentation

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: AuthRepository,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(SplashScreenState())
    val state = _state.toCommonStateFlow()

    init {
        authenticate()
    }

    private fun authenticate() {
        viewModelScope.launch {
            val result = repository.authenticate()
            _state.update { it.copy(isAuthorized = result is AuthResult.Authorized) }
        }
    }
}