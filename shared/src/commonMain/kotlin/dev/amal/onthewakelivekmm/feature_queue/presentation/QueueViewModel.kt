package dev.amal.onthewakelivekmm.feature_queue.presentation

import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueSocketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QueueViewModel(
    private val queueService: QueueService,
    private val queueSocketService: QueueSocketService,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(QueueState())
    val state = _state.toCommonStateFlow()

    init {
        connectToQueue()
    }

    private fun connectToQueue() {
        getQueue()

        viewModelScope.launch {
            when (val result = queueSocketService.initSession("Guest")) {
                is Resource.Success -> {
                    observeQueue()
                    println("Success")
                }
                is Resource.Error -> _state.update {
                    it.copy(error = result.message)
                }
            }
        }
    }

    private fun observeQueue() {
        queueSocketService.observeQueue()
            .onEach { queueItem ->
                val newList = state.value.queue
                    .toMutableList()
                    .apply {
                        if (queueItem.isDeleteAction) removeAll { it.id == queueItem.queueItem.id }
                        else add(0, queueItem.queueItem.toQueueItemState())
                    }
                    .sortedWith(compareByDescending { it.timestamp })
                _state.update { it.copy(queue = newList) }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: QueueSocketEvent) {
        when (event) {
            is QueueSocketEvent.AddToQueue -> {
                addToQueue(
                    isLeftQueue = event.isLeftQueue,
                    firstName = event.firstName,
                    timestamp = event.timestamp
                )
            }
            is QueueSocketEvent.DeleteQueueItem -> {
                deleteQueueItem(queueItemId = event.queueItemId)
            }
        }
    }

    private fun addToQueue(isLeftQueue: Boolean, firstName: String, timestamp: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isQueueLoading = true) }
            queueSocketService.addToQueue(isLeftQueue, firstName, timestamp)
            _state.update { it.copy(isQueueLoading = false) }
        }
    }

    private fun getQueue() {
        viewModelScope.launch {
            _state.update { it.copy(isQueueLoading = true) }
            val result = queueService.getQueue()
            _state.update { queueState ->
                queueState.copy(
                    queue = result.map { it.toQueueItemState() },
                    isQueueLoading = false
                )
            }
        }
    }

    private fun deleteQueueItem(queueItemId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(isQueueLoading = true)
            when (val result = queueService.deleteQueueItem(queueItemId)) {
                is Resource.Success -> {
                    result.data?.let { deletedItem ->
                        println("deleted item $deletedItem")
                    }
                }
                is Resource.Error -> _state.update {
                    it.copy(error = result.message)
                }
            }
            _state.value = state.value.copy(isQueueLoading = false)
        }
    }
}