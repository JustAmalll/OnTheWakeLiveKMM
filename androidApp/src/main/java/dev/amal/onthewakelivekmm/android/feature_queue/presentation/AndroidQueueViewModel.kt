package dev.amal.onthewakelivekmm.android.feature_queue.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueSocketService
import dev.amal.onthewakelivekmm.feature_queue.presentation.QueueSocketEvent
import dev.amal.onthewakelivekmm.feature_queue.presentation.QueueViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidQueueViewModel @Inject constructor(
    private val queueService: QueueService,
    private val queueSocketService: QueueSocketService
): ViewModel() {

    private val viewModel by lazy {
        QueueViewModel(
            queueService = queueService,
            queueSocketService = queueSocketService,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: QueueSocketEvent) {
        viewModel.onEvent(event)
    }
}