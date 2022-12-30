package dev.amal.onthewakelivekmm.feature_queue.presentation

data class QueueState(
    var isQueueLoading: Boolean = false,
    val queue: List<QueueItemState> = emptyList(),
    val error: String? = null
)