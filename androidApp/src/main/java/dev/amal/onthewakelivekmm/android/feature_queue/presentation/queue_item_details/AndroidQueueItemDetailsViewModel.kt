package dev.amal.onthewakelivekmm.android.feature_queue.presentation.queue_item_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.android.core.utils.Constants.DETAILS_ARGUMENT_KEY
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import dev.amal.onthewakelivekmm.feature_queue.presentation.queue_item_details.QueueItemDetailsEvent
import dev.amal.onthewakelivekmm.feature_queue.presentation.queue_item_details.QueueItemDetailsViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidQueueItemDetailsViewModel @Inject constructor(
    private val queueService: QueueService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val viewModel by lazy {
        QueueItemDetailsViewModel(
            queueService = queueService,
            coroutineScope = viewModelScope
        )
    }

    init {
        savedStateHandle.get<String>(DETAILS_ARGUMENT_KEY)?.let { queueItemId ->
            onEvent(QueueItemDetailsEvent.LoadQueueItemDetails(queueItemId))
        }
    }

    val state = viewModel.state

    fun onEvent(event: QueueItemDetailsEvent) {
        viewModel.onEvent(event)
    }
}