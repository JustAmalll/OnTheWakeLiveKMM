package dev.amal.onthewakelivekmm.feature_queue.presentation

import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

data class QueueState(
    val queue: List<QueueItem> = emptyList(),
    var isQueueLoading: Boolean = false
)