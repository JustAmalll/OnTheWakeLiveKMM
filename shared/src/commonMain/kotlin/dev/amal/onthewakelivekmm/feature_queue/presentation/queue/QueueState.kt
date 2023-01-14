package dev.amal.onthewakelivekmm.feature_queue.presentation.queue

import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

data class QueueState(
    var isQueueLoading: Boolean = false,
    val userId: String? = null,
    val queue: List<QueueItem> = emptyList(),
    val error: String? = null
)