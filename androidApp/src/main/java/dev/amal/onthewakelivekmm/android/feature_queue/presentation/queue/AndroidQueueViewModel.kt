package dev.amal.onthewakelivekmm.android.feature_queue.presentation.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueSocketService
import dev.amal.onthewakelivekmm.feature_queue.presentation.queue.QueueEvent
import dev.amal.onthewakelivekmm.feature_queue.presentation.queue.QueueViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidQueueViewModel @Inject constructor(
    private val queueService: QueueService,
    private val queueSocketService: QueueSocketService,
    private val preferenceManager: PreferenceManager,
) : ViewModel() {

    private val viewModel by lazy {
        QueueViewModel(
            queueService = queueService,
            queueSocketService = queueSocketService,
            preferenceManager = preferenceManager,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: QueueEvent) {
        viewModel.onEvent(event)
    }
}