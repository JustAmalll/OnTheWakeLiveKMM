package dev.amal.onthewakelivekmm.feature_queue.presentation

data class QueueState(
    var isQueueLoading: Boolean = false,
    val userId: String? = null,
    val queue: List<QueueItemState> = emptyList(),
    val error: String? = null
)