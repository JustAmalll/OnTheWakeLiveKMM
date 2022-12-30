package dev.amal.onthewakelivekmm.feature_queue.presentation

data class QueueState(
    val queue: List<QueueItemState> = emptyList(),
    var isQueueLoading: Boolean = false
)