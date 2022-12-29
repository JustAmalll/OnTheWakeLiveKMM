package dev.amal.onthewakelivekmm.android.feature_queue.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import dev.amal.onthewakelivekmm.feature_queue.presentation.QueueViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidQueueViewModel @Inject constructor(
    private val queueService: QueueService
): ViewModel() {

    private val viewModel by lazy {
        QueueViewModel(
            queueService = queueService,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state
}