package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_login.LoginEvent
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_login.LoginViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidLoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val validationUseCase: ValidationUseCase,
) : ViewModel() {

    private val viewModel by lazy {
        LoginViewModel(
            repository = repository,
            validationUseCase = validationUseCase,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: LoginEvent) {
        viewModel.onEvent(event)
    }
}