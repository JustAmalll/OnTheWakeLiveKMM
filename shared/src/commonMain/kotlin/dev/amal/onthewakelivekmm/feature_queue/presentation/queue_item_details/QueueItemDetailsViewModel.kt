package dev.amal.onthewakelivekmm.feature_queue.presentation.queue_item_details

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QueueItemDetailsViewModel(
    private val queueService: QueueService,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(QueueItemDetailsState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: QueueItemDetailsEvent) {
        when (event) {
            is QueueItemDetailsEvent.LoadQueueItemDetails -> {
                loadQueueItemDetails(event.queueItemId)
            }
            QueueItemDetailsEvent.OnErrorSeen -> _state.update {
                it.copy(error = null)
            }
        }
    }

    private fun loadQueueItemDetails(queueItemId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = queueService.getProfileDetails(queueItemId)) {
                is Resource.Success -> {
                    result.data?.let { profile ->
                        _state.update { it.copy(profile = profile) }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(error = result.message ?: "Unknown Error") }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}