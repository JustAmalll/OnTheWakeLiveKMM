package dev.amal.onthewakelivekmm.feature_queue.domain.module

import kotlinx.serialization.Serializable

@Serializable
data class QueueResponse(
    val isDeleteAction: Boolean,
    val queueItem: QueueItem
)