package dev.amal.onthewakelivekmm.feature_queue.presentation.queue

import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.domain.util.toCommonStateFlow
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_USER_ID
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
    preferenceManager: PreferenceManager,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(QueueState())
    val state = _state.toCommonStateFlow()

    private val userId = preferenceManager.getString(PREFS_USER_ID)

    init {
        connectToQueue()
        _state.update { it.copy(userId = userId) }
    }

    private fun connectToQueue() {
        getQueue()

        viewModelScope.launch {
            when (val result = queueSocketService.initSession()) {
                is Resource.Success -> {
                    observeQueue()
                }
                is Resource.Error -> _state.update {
                    it.copy(error = result.message ?: "An unknown error occurred")
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
                        else add(0, queueItem.queueItem)
                    }
                    .sortedWith(compareByDescending { it.timestamp })
                _state.update { it.copy(queue = newList) }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: QueueEvent) {
        when (event) {
            is QueueEvent.AddToQueue -> {
                addToQueue(
                    isLeftQueue = event.isLeftQueue,
                    firstName = event.firstName
                )
            }
            is QueueEvent.DeleteQueueItem -> {
                deleteQueueItem(queueItemId = event.queueItemId)
            }
            is QueueEvent.OnQueueErrorSeen -> _state.update {
                it.copy(error = null)
            }
        }
    }

    private fun addToQueue(
        isLeftQueue: Boolean, firstName: String? = null
    ) {
        val canAddToQueue = queueSocketService.canAddToQueue(
            isLeftQueue = isLeftQueue, queue = _state.value.queue
        )
        val addToQueueError = canAddToQueue.message

        if (canAddToQueue is Resource.Success) viewModelScope.launch {
            val result = queueSocketService.addToQueue(
                isLeftQueue = isLeftQueue, firstName = firstName
            )
            _state.update { it.copy(error = result.message) }
        } else {
            _state.update { it.copy(error = addToQueueError) }
        }
    }

    private fun getQueue() {
        viewModelScope.launch {
            _state.update { it.copy(isQueueLoading = true) }
            val result = queueService.getQueue()
            _state.update { queueState ->
                queueState.copy(queue = result, isQueueLoading = false)
            }
        }
    }

    private fun deleteQueueItem(queueItemId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(isQueueLoading = true)
            when (val result = queueService.deleteQueueItem(queueItemId)) {
                is Resource.Success -> Unit
                is Resource.Error -> _state.update {
                    it.copy(error = result.message ?: "An unknown error occurred")
                }
            }
            _state.value = state.value.copy(isQueueLoading = false)
        }
    }
}