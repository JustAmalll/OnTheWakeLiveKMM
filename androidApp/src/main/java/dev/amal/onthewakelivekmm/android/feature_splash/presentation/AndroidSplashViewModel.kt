package dev.amal.onthewakelivekmm.android.feature_splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.feature_auth.domain.repository.AuthRepository
import dev.amal.onthewakelivekmm.feature_splash.presentation.SplashViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidSplashViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val viewModel by lazy {
        SplashViewModel(
            repository = repository,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state
}